import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

public class FileManager {
	public static ArrayList<ArrayList<String>> cleanTags(Connection conn, String query,
			String sortedUniqueCleanTagsFile, String cleanRowsFile, String junkFileName)
					throws SQLException, IOException {

		ArrayList<ArrayList<String>> cleanRowsList = new ArrayList<>();

		Statement st = conn.createStatement();

		ResultSet rs = st.executeQuery(query);

		ArrayList<String> hashTagSortedUniqueList = new ArrayList<>();
		ArrayList<String> junkTagList = new ArrayList<>();
		HashSet<String> hashTagSet = new HashSet<>();
		HashSet<String> uniqueTweets = new HashSet<>();

		Locale locale = Locale.ENGLISH;

		while (rs.next()) {
			String hashTags = rs.getString("hashtags");
			String location = rs.getString("location");
			int followersCount = rs.getInt("followers_count");
			String lang = rs.getString("lang");

			String concated = hashTags + "#" + location + "#" + followersCount + "#" + lang;
			if (!uniqueTweets.contains(concated)) {
				uniqueTweets.add(concated);
			} else {
				continue;
			}

			String[] tagArr = hashTags.split(",");

			ArrayList<String> row = new ArrayList<>();

			for (int i = 0; i < tagArr.length; i++) {
				String currTag = tagArr[i].toLowerCase(locale).trim();
				if (currTag.length() < 3 || !currTag.matches("\\w+")) {
					junkTagList.add(currTag);
				} else {
					row.add(currTag);

					if (!hashTagSet.contains(currTag)) {
						hashTagSet.add(currTag);
						hashTagSortedUniqueList.add(currTag);
					}
				}
			}
			if (!row.isEmpty()) {
				cleanRowsList.add(row);
			}
		}

		st.close();

		// clean file
		Collections.sort(hashTagSortedUniqueList);
		Path file = Paths.get(sortedUniqueCleanTagsFile);
		Files.write(file, hashTagSortedUniqueList, Charset.forName("UTF-8"));

		// clean rows
		BufferedWriter outputWriter = null;
		outputWriter = new BufferedWriter(new FileWriter(cleanRowsFile));

		for (ArrayList<String> row : cleanRowsList) {
			outputWriter.write(String.join(" ", row));
			outputWriter.newLine();
		}

		outputWriter.flush();
		outputWriter.close();

		// junk file
		Path junkFile = Paths.get(junkFileName);
		Files.write(junkFile, junkTagList, Charset.forName("UTF-8"));

		return cleanRowsList;
	}

	public static ArrayList<HashMap<String, String>> getFormattedList(String sourceFile) throws IOException {
		HashMap<String, String> map = new HashMap<>();
		HashMap<String, String> reverseMap = new HashMap<>();
		ArrayList<HashMap<String, String>> returnVal = new ArrayList<>();

		int lastId = 1;

		FileReader fr = new FileReader(sourceFile);
		BufferedReader br = new BufferedReader(fr);
		String line;
		while ((line = br.readLine()) != null) {
			map.put(line, Integer.toString(lastId));
			reverseMap.put(Integer.toString(lastId), line);
			lastId++;
		}
		br.close();
		returnVal.add(map);
		returnVal.add(reverseMap);

		return returnVal;
	}

	public static void buildPreparedDataFile(HashMap<String, String> uniqueHashTagsWithIds,
			ArrayList<ArrayList<String>> cleanRows, String preparedFile) throws Exception {

		ArrayList<String> idList;
		BufferedWriter outputWriter = null;
		outputWriter = new BufferedWriter(new FileWriter(preparedFile));

		for (ArrayList<String> row : cleanRows) {
			idList = new ArrayList<>();
			for (String tag : row) {
				String id = uniqueHashTagsWithIds.get(tag);
				idList.add(id);
			}
			if (idList.isEmpty())
				continue;
			outputWriter.write(String.join(" ", idList));
			outputWriter.newLine();
		}

		outputWriter.flush();
		outputWriter.close();

	}

	public static ArrayList<ArrayList<AprioryRecord>> restoreFileBySPMFAprioryResults(String inputFile,
			HashMap<String, String> idsWithHashTags) throws IOException {

		ArrayList<ArrayList<AprioryRecord>> levels = new ArrayList<>();
		ArrayList<AprioryRecord> level = new ArrayList<>();

		Charset charset = Charset.forName("UTF-8");
		BufferedReader br = null;
		br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), charset));

		String line;
		boolean reachedSupport;
		int tagCount = 1;
		while ((line = br.readLine()) != null) {
			AprioryRecord record = new AprioryRecord();
			reachedSupport = false;

			String[] parts = line.split(" ");

			for (String part : parts) {
				if (part.isEmpty())
					continue;

				if (part.equals("#SUP:")) {
					reachedSupport = true;
					continue;
				}

				if (!reachedSupport) {
					String tag = idsWithHashTags.get(part);
					record.pushTag(tag);
				} else {
					record.setSupport(Integer.parseInt(part));
				}
			}
			if (record.getTags().size() > tagCount) {
				tagCount = record.getTags().size();
				levels.add(level);
				level = new ArrayList<>();
			}
			level.add(record);
		}
		levels.add(level);

		br.close();

		for (ArrayList<AprioryRecord> subLevel : levels) {
			Collections.sort(subLevel, new Comparator<AprioryRecord>() {
				@Override
				public int compare(AprioryRecord lhs, AprioryRecord rhs) {

					return rhs.getSupport() - lhs.getSupport();
				}
			});
		}

		return levels;
	}

	public static ArrayList<ArrayList<AssociationRecord>> restoreFileBySPMFFPGAssocResults(String inputFile,
			HashMap<String, String> idsWithHashTags) throws NumberFormatException, IOException {

		ArrayList<ArrayList<AssociationRecord>> levels = new ArrayList<>();
		ArrayList<AssociationRecord> level = new ArrayList<>();

		Charset charset = Charset.forName("UTF-8");
		BufferedReader br = null;
		br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), charset));

		String line;
		boolean reachedArrow;
		boolean reachedSupport;
		boolean reachedConf;

		int tagCount = 2;
		while ((line = br.readLine()) != null) {
			AssociationRecord record = new AssociationRecord();
			reachedArrow = false;
			reachedSupport = false;
			reachedConf = false;

			String[] parts = line.split(" ");

			for (String part : parts) {
				if (part.isEmpty())
					continue;

				if (part.equals("==>")) {
					reachedArrow = true;
					continue;
				}

				if (part.equals("#SUP:")) {
					reachedSupport = true;
					continue;
				}

				if (part.equals("#CONF:")) {
					reachedConf = true;
					continue;
				}

				if (!reachedArrow && !reachedSupport && !reachedConf) {
					String tag = idsWithHashTags.get(part);
					record.pushTagLHS(tag);
				}

				if (reachedArrow && !reachedSupport && !reachedConf) {
					String tag = idsWithHashTags.get(part);
					record.pushTagRHS(tag);
				}

				if (reachedSupport && !reachedConf) {
					record.setSupport(Integer.parseInt(part));
				}

				if (reachedConf) {
					record.setConfidence(Double.parseDouble(part));
				}
			}
			if (record.getAllTags().size() > tagCount) {
				tagCount = record.getAllTags().size();
				levels.add(level);
				level = new ArrayList<>();
			}
			level.add(record);
		}
		levels.add(level);

		br.close();

		for (ArrayList<AssociationRecord> subLevel : levels) {
			Collections.sort(subLevel, new Comparator<AssociationRecord>() {
				@Override
				public int compare(AssociationRecord lhs, AssociationRecord rhs) {
					return rhs.getSupport() - lhs.getSupport();
				}
			});
		}

		return levels;
	}
}
