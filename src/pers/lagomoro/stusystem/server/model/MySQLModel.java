package pers.lagomoro.stusystem.server.model;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;

import pers.lagomoro.stusystem.data.DataChat;
import pers.lagomoro.stusystem.data.DataFile;
import pers.lagomoro.stusystem.data.DataNotice;
import pers.lagomoro.stusystem.data.DataPaintAndGuess;
import pers.lagomoro.stusystem.data.DataPerusal;
import pers.lagomoro.stusystem.data.DataProfileImage;
import pers.lagomoro.stusystem.data.DataUser;

public class MySQLModel {
	
	public final static String SQL_USER = "root";
	public final static String SQL_PASSWARD = "ghost2k3";
	public final static String SQL_URL = "jdbc:mysql://localhost:3306/student_management_system?useSSL=true&serverTimezone=GMT%2B8&characterEncoding=utf8";
	
	public static final String SERVER_BASE = "./server/";
	public final static String FILE_PATH = SERVER_BASE + "file/";
	
	public static Connection connection;
	
	public static PreparedStatement statementGetUser;
	public static PreparedStatement statementInsertUser;
	public static PreparedStatement statementCreateUserTable;
	public static PreparedStatement statementUpdateUserTable;
	public static PreparedStatement statementReplaceUserPassword;
	public static PreparedStatement statementUpdateUserPassword;
	public static PreparedStatement statementUpdateUserNickname;
	public static PreparedStatement statementUpdateUserImageUrl;
	public static PreparedStatement statementUpdateUserClassId;
	public static PreparedStatement statementUpdateUserAuthority;
	
	public static PreparedStatement statementSelectUserTable;
	
	public static PreparedStatement statementGetClass;
	public static PreparedStatement statementInsertClass;
	public static PreparedStatement statementCreateClassTable;
	public static PreparedStatement statementUpdateClassTable;
	public static PreparedStatement statementUpdateClassClassname;
	
	public static PreparedStatement statementSelectClassTable;
	
	public static PreparedStatement statementGetPaintAndGuess;
	public static void run() {
		try {
			File documents = new File(FILE_PATH);
	        if (!documents.exists()) documents.mkdir();
	        
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(SQL_URL, SQL_USER, SQL_PASSWARD);
			statementGetUser             = connection.prepareStatement("SELECT * FROM user WHERE username = ?");
			statementInsertUser          = connection.prepareStatement("INSERT INTO user (username, password, nickname, image_data, class_id, authority) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			statementReplaceUserPassword = connection.prepareStatement("UPDATE user SET password = REPLACE (password, ?, ?) WHERE username = ?");
			statementUpdateUserPassword  = connection.prepareStatement("UPDATE user SET password = ? WHERE username = ?");
			statementUpdateUserNickname  = connection.prepareStatement("UPDATE user SET nickname = ? WHERE username = ?");
			statementUpdateUserImageUrl  = connection.prepareStatement("UPDATE user SET image_data = ? WHERE username = ?");
			statementUpdateUserClassId   = connection.prepareStatement("UPDATE user SET class_id = ? WHERE username = ?");
			statementUpdateUserAuthority = connection.prepareStatement("UPDATE user SET authority = ? WHERE username = ?");
			
			statementGetClass             = connection.prepareStatement("SELECT * FROM class WHERE id = ?");
			statementInsertClass          = connection.prepareStatement("INSERT INTO class (classname) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			statementUpdateClassClassname = connection.prepareStatement("UPDATE class SET classname = ? WHERE id = ?");

			statementGetPaintAndGuess     = connection.prepareStatement("SELECT * FROM paint_and_guess");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() {
		return connection;
	}
	
	public static Timestamp getTimestamp() {
		return new Timestamp(new Date().getTime());
	}
	
	public static boolean haveTable(String table_name) {
		try {
			ResultSet resultSet = connection.getMetaData().getTables(null, null, table_name, null);
			boolean have = resultSet.next();
			resultSet.close();
			return have;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * =========================================================================================
	 */
	
	public static boolean haveUser(String username) {
		try {
			statementGetUser.setString(1, username);
			ResultSet resultSet = statementGetUser.executeQuery();
			boolean have = resultSet.next();
			resultSet.close();
			return have;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static ResultSet getUser(String username) {
		try {
			statementGetUser.setString(1, username);
			return statementGetUser.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @return
	 * 0: Success 
	 * 1: Current user already exist.
	 */
	public static int insertUser(String username, String password) {
		return insertUser(username, password, username, "", 0, false);
	}
	public static int insertUser(String username, String password, String nickname, String image_data, int class_id, boolean authority) {
		try {
			statementInsertUser.setString(1, username);
			statementInsertUser.setString(2, password);
			statementInsertUser.setString(3, nickname);
			statementInsertUser.setString(4, image_data);
			statementInsertUser.setInt(5, class_id);
			statementInsertUser.setBoolean(6, authority);
			statementInsertUser.executeUpdate();
			ResultSet resultSet = statementInsertUser.getGeneratedKeys(); 
			if(resultSet.next()) {
				createUserChatTable(username);
				resultSet.close();
				return 0;
			}else {
				resultSet.close();
				return 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	public static void createUserChatTable(String username) {
		try {
			if(!haveTable("user_" + username + "_chat")){
				statementCreateUserTable = connection.prepareStatement("CREATE TABLE user_" + username + "_chat (id INT NOT NULL AUTO_INCREMENT, username VARCHAR(16) NOT NULL, document MEDIUMTEXT NOT NULL, withdraw TINYINT(1) NOT NULL DEFAULT 0, timestamp DATETIME(3) NOT NULL, PRIMARY KEY (id)) ENGINE = InnoDB;");
				statementCreateUserTable.executeLargeUpdate();
				statementCreateUserTable.close();
				statementCreateUserTable = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return
	 * 0: Success 
	 * 1: Unknown user.
	 * 2: Invalid old password.
	 */
	public static int resetPassword(String username, String oldPassword, String newPassword) {
		try {
			if(!haveUser(username)) {
				return 1;
			} else {
				ResultSet resultSet = getUser(username);
				resultSet.next();
				if(!resultSet.getString("password").equals(oldPassword)) {
					resultSet.close();
					return 2;
				}else {
					resultSet.close();
					statementReplaceUserPassword.setString(1, oldPassword);
					statementReplaceUserPassword.setString(2, newPassword);
					statementReplaceUserPassword.setString(3, username);
					return 1 - statementReplaceUserPassword.executeUpdate();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	/**
	 * @return
	 * 0: Success 
	 * 1: Unknown user.
	 */
	public static int updatePassword(String username, String password) {
		try {
			if(!haveUser(username)) {
				return 1;
			} else {
				statementUpdateUserPassword.setString(1, password);
				statementUpdateUserPassword.setString(2, username);
				return 1 - statementUpdateUserPassword.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	/**
	 * @return
	 * 0: Success 
	 * 1: Unknown user.
	 */
	public static int updateNickname(String username, String nickname) {
		try {
			if(!haveUser(username)) {
				return 1;
			} else {
				statementUpdateUserNickname.setString(1, nickname);
				statementUpdateUserNickname.setString(2, username);
				return 1 - statementUpdateUserNickname.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	/**
	 * @return
	 * 0: Success 
	 * 1: Unknown user.
	 */
	public static int updateImageData(String username, String image_data) {
		try {
			if(!haveUser(username)) {
				return 1;
			} else {
				statementUpdateUserImageUrl.setString(1, image_data);
				statementUpdateUserImageUrl.setString(2, username);
				return 1 - statementUpdateUserImageUrl.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	/**
	 * @return
	 * 0: Success 
	 * 1: Unknown user.
	 */
	public static int updateClassID(String username, int class_id) {
		try {
			if(!haveUser(username)) {
				return 1;
			} else {
				statementUpdateUserClassId.setInt(1, class_id);
				statementUpdateUserClassId.setString(2, username);
				return 1 - statementUpdateUserClassId.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	/**
	 * @return
	 * 0: Success 
	 * 1: Unknown user.
	 */
	public static int updateAuthority(String username, boolean authority) {
		try {
			if(!haveUser(username)) {
				return 1;
			} else {
				statementUpdateUserAuthority.setBoolean(1, authority);
				statementUpdateUserAuthority.setString(2, username);
				return 1 - statementUpdateUserAuthority.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	/**
	 * @return
	 * 0: Success 
	 * 1: Unknown user.
	 * 2: Invalid password.
	 */
	public static int checkLogin(String username, String password) {
		try {
			if(!haveUser(username)) {
				return 1;
			} else {
				ResultSet resultSet = getUser(username);
				resultSet.next();
				if(resultSet.getString("password").equals(password)) {
					resultSet.close();
					return 0;
				}else {
					resultSet.close();
					return 2;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	/**
	 * =========================================================================================
	 */
	
	public static boolean haveClass(int class_id) {
		try {
			statementGetClass.setInt(1, class_id);
			ResultSet resultSet = statementGetClass.executeQuery();
			boolean have = resultSet.next();
			resultSet.close();
			return have;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static ResultSet getClass(int class_id) {
		try {
			statementGetClass.setInt(1, class_id);
			return statementGetClass.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ResultSet getClassId(int class_id) {
		try {
			statementGetClass.setInt(1, class_id);
			return statementGetClass.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @return
	 * 0: Success 
	 * 1: Current class already exist.
	 */
	public static int insertClass(String classname) {
		try {
			statementInsertClass.setString(1, classname);
			statementInsertClass.executeUpdate();
			ResultSet resultSet = statementInsertClass.getGeneratedKeys(); 
			if(resultSet.next()) {
				int class_id = resultSet.getInt(1);
				createClassChatTable(class_id);
				createClassNoticeTable(class_id);
				createClassFileTable(class_id);
				createClassPerusalTable(class_id);
				resultSet.close();
				return 0;
			}else {
				resultSet.close();
				return 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	public static void createClassChatTable(int class_id) {
		try {
			if(!haveTable("class_" + class_id + "_chat")){
				statementCreateClassTable = connection.prepareStatement("CREATE TABLE class_" + class_id + "_chat (id INT NOT NULL AUTO_INCREMENT, username VARCHAR(16) NOT NULL, document MEDIUMTEXT NOT NULL, withdraw TINYINT(1) NOT NULL DEFAULT 0, timestamp DATETIME(3) NOT NULL, PRIMARY KEY (id)) ENGINE = InnoDB;");
				statementCreateClassTable.executeLargeUpdate();
				statementCreateClassTable.close();
				statementCreateClassTable = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void createClassNoticeTable(int class_id) {
		try {
			if(!haveTable("class_" + class_id + "_notice")){
				statementCreateClassTable = connection.prepareStatement("CREATE TABLE class_" + class_id + "_notice (id INT NOT NULL AUTO_INCREMENT, username VARCHAR(16) NOT NULL, title VARCHAR(64) NOT NULL, document MEDIUMTEXT NOT NULL, timestamp DATETIME(3) NOT NULL, PRIMARY KEY (id)) ENGINE = InnoDB;");
				statementCreateClassTable.executeLargeUpdate();
				statementCreateClassTable.close();
				statementCreateClassTable = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void createClassFileTable(int class_id) {
		try {
			if(!haveTable("class_" + class_id + "_file")){
				statementCreateClassTable = connection.prepareStatement("CREATE TABLE class_" + class_id + "_file (id INT NOT NULL AUTO_INCREMENT, username VARCHAR(16) NOT NULL, filename VARCHAR(64) NOT NULL, size BIGINT NOT NULL, path VARCHAR(128) NOT NULL, timestamp DATETIME(3) NOT NULL, PRIMARY KEY (id)) ENGINE = InnoDB;");
				statementCreateClassTable.executeLargeUpdate();
				statementCreateClassTable.close();
				statementCreateClassTable = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void createClassPerusalTable(int class_id) {
		try {
			if(!haveTable("class_" + class_id + "_perusal")){
				statementCreateClassTable = connection.prepareStatement("CREATE TABLE class_" + class_id + "_perusal (id INT NOT NULL AUTO_INCREMENT, username VARCHAR(16) NOT NULL, title VARCHAR(63) NOT NULL, document MEDIUMTEXT NOT NULL, timestamp DATETIME(3) NOT NULL, max_choose INT(4) NOT NULL, options VARCHAR(2048) NOT NULL, chooses VARCHAR(2048) NOT NULL, comments VARCHAR(4096) NOT NULL, PRIMARY KEY (id)) ENGINE = InnoDB;");
				statementCreateClassTable.executeLargeUpdate();
				statementCreateClassTable.close();
				statementCreateClassTable = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return
	 * 0: Success 
	 * 1: Unknown class.
	 */
	public static int updateClassClassname(int class_id, String classname) {
		try {
			if(!haveClass(class_id)) {
				return 1;
			} else {
				statementUpdateClassClassname.setString(1, classname);
				statementUpdateClassClassname.setInt(2, class_id);
				return 1 - statementUpdateClassClassname.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	/**
	 * =========================================================================================
	 */
	
	/**
	 * @return
	 * 0: Success 
	 * 1: Unknown chat.
	 */
	public static int insertUserChat(String username, String username_to, String text, boolean withdraw, Timestamp timestamp) {
		try {
			if(!haveUser(username) || !haveUser(username_to) || !haveTable("user_" + username + "_chat")) {
				return 1;
			} else {
				statementUpdateUserTable = connection.prepareStatement("INSERT INTO user_" + username + "_chat (username, document, withdraw, timestamp) VALUES (?, ?, ?, ?)");
				statementUpdateUserTable.setString(1, username_to);
				statementUpdateUserTable.setString(2, text);
				statementUpdateUserTable.setBoolean(3, withdraw);
				statementUpdateUserTable.setTimestamp(4, timestamp);
				int result = 1 - statementUpdateUserTable.executeUpdate();
				statementUpdateUserTable.close();
				statementUpdateUserTable = null;
				return result;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	/**
	 * @return
	 * 0: Success 
	 * 1: Unknown chat.
	 */
	public static int updateUserChatWithdraw(String username, int id, boolean withdraw) {
		try {
			if(!haveTable("user_" + username + "_chat")) {
				return 1;
			} else {
				statementUpdateUserTable = connection.prepareStatement("UPDATE user_" + username + "_chat SET withdraw = ? WHERE id = ?");
				statementUpdateUserTable.setBoolean(1, withdraw);
				statementUpdateUserTable.setInt(2, id);
				int result = 1 - statementUpdateUserTable.executeUpdate();
				statementUpdateUserTable.close();
				statementUpdateUserTable = null;
				return result;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	/**
	 * @return
	 * 0: Success 
	 * 1: Unknown chat.
	 */
	public static int insertClassChat(int class_id, String username, String text, boolean withdraw, Timestamp timestamp) {
		try {
			if(!haveClass(class_id) || !haveUser(username) || !haveTable("class_" + class_id + "_chat")) {
				return 1;
			} else {
				statementUpdateClassTable = connection.prepareStatement("INSERT INTO class_" + class_id + "_chat (username, document, withdraw, timestamp) VALUES (?, ?, ?, ?)");
				statementUpdateClassTable.setString(1, username);
				statementUpdateClassTable.setString(2, text);
				statementUpdateClassTable.setBoolean(3, withdraw);
				statementUpdateClassTable.setTimestamp(4, timestamp);
				int result = 1 - statementUpdateClassTable.executeUpdate();
				statementUpdateClassTable.close();
				statementUpdateClassTable = null;
				return result;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	/**
	 * @return
	 * 0: Success 
	 * 1: Unknown chat.
	 */
	public static int updateClassChatWithdraw(int class_id, int id, boolean withdraw) {
		try {
			if(!haveTable("class_" + class_id + "_chat")) {
				return 1;
			} else {
				statementUpdateClassTable = connection.prepareStatement("UPDATE class_" + class_id + "_chat SET withdraw = ? WHERE id = ?");
				statementUpdateClassTable.setBoolean(1, withdraw);
				statementUpdateClassTable.setInt(2, id);
				int result = 1 - statementUpdateClassTable.executeUpdate();
				statementUpdateClassTable.close();
				statementUpdateClassTable = null;
				return result;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	/**
	 * @return
	 * 0: Success 
	 * 1: Unknown notice.
	 */
	public static int insertClassNotice(int class_id, String username, String title, String document, Timestamp timestamp) {
		try {
			if(!haveTable("class_" + class_id + "_notice")) {
				return 1;
			} else {
				statementUpdateClassTable = connection.prepareStatement("INSERT INTO class_" + class_id + "_notice (username, title, document, timestamp) VALUES (?, ?, ?, ?)");
				statementUpdateClassTable.setString(1, username);
				statementUpdateClassTable.setString(2, title);
				statementUpdateClassTable.setString(3, document);
				statementUpdateClassTable.setTimestamp(4, timestamp);
				int result = 1 - statementUpdateClassTable.executeUpdate();
				statementUpdateClassTable.close();
				statementUpdateClassTable = null;
				return result;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	/**
	 * @return
	 * 0: Success 
	 * 1: Unknown notice.
	 */
	public static int deleteClassNotice(int class_id, int id) {
		try {
			if(!haveTable("class_" + class_id + "_notice")) {
				return 1;
			} else {
				statementUpdateClassTable = connection.prepareStatement("DELETE FROM class_" + class_id + "_notice WHERE id = ?");
				statementUpdateClassTable.setInt(1, id);
				int result = 1 - statementUpdateClassTable.executeUpdate();
				statementUpdateClassTable.close();
				statementUpdateClassTable = null;
				return result;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	/**
	 * @return
	 * 0: Success 
	 * 1: Unknown file.
	 */
	public static int insertClassFile(int class_id, String username, String filename, long size, String path, Timestamp timestamp) {
		try {
			if(!haveTable("class_" + class_id + "_file")) {
				return 1;
			} else {
				statementUpdateClassTable = connection.prepareStatement("INSERT INTO class_" + class_id + "_file (username, filename, size, path, timestamp) VALUES (?, ?, ?, ?, ?)");
				statementUpdateClassTable.setString(1, username);
				statementUpdateClassTable.setString(2, filename);
				statementUpdateClassTable.setLong(3, size);
				statementUpdateClassTable.setString(4, path);
				statementUpdateClassTable.setTimestamp(5, timestamp);
				int result = 1 - statementUpdateClassTable.executeUpdate();
				statementUpdateClassTable.close();
				statementUpdateClassTable = null;
				return result;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	/**
	 * @return
	 * 0: Success 
	 * 1: Unknown file.
	 */
	public static int deleteClassFile(int class_id, int id) {
		try {
			if(!haveTable("class_" + class_id + "_file")) {
				return 1;
			} else {
				statementUpdateClassTable = connection.prepareStatement("DELETE FROM class_" + class_id + "_file WHERE id = ?");
				statementUpdateClassTable.setInt(1, id);
				int result = 1 - statementUpdateClassTable.executeUpdate();
				statementUpdateClassTable.close();
				statementUpdateClassTable = null;
				return result;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	/**
	 * @return
	 * 0: Success 
	 * 1: Unknown perusal.
	 */
	public static int insertClassPerusal(int class_id, String username, String title, String document, Timestamp timestamp, int max_choose, String options) {
		try {
			if(!haveTable("class_" + class_id + "_perusal")) {
				return 1;
			} else {
				statementUpdateClassTable = connection.prepareStatement("INSERT INTO class_" + class_id + "_perusal (username, title, document, timestamp, max_choose, options, chooses, comments) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
				statementUpdateClassTable.setString(1, username);
				statementUpdateClassTable.setString(2, title);
				statementUpdateClassTable.setString(3, document);
				statementUpdateClassTable.setTimestamp(4, timestamp);
				statementUpdateClassTable.setInt(5, max_choose);
				statementUpdateClassTable.setString(6, options);
				statementUpdateClassTable.setString(7, ";-");
				statementUpdateClassTable.setString(8, ";-");
				int result = 1 - statementUpdateClassTable.executeUpdate();
				statementUpdateClassTable.close();
				statementUpdateClassTable = null;
				return result;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	/**
	 * @return
	 * 0: Success 
	 * 1: Unknown perusal.
	 */
	public static int insertClassPerusalChoose(int class_id, int id, String vote) {
		try {
			if(!haveTable("class_" + class_id + "_perusal")) {
				return 1;
			} else {
				statementUpdateClassTable = connection.prepareStatement("UPDATE class_" + class_id + "_perusal SET chooses = REPLACE (chooses, ?, ?) WHERE id = ?");
				statementUpdateClassTable.setString(1, ";-");
				statementUpdateClassTable.setString(2, ";" + vote + ";-");
				statementUpdateClassTable.setInt(3, id);
				int result = 1 - statementUpdateClassTable.executeUpdate();
				statementUpdateClassTable.close();
				statementUpdateClassTable = null;
				return result;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	/**
	 * @return
	 * 0: Success 
	 * 1: Unknown perusal.
	 */
	public static int insertClassPerusalComment(int class_id, int id, String comment) {
		try {
			if(!haveTable("class_" + class_id + "_perusal")) {
				return 1;
			} else {
				statementUpdateClassTable = connection.prepareStatement("UPDATE class_" + class_id + "_perusal SET comments = REPLACE (comments, ?, ?) WHERE id = ?");
				statementUpdateClassTable.setString(1, ";-");
				statementUpdateClassTable.setString(2, ";" + comment + ";-");
				statementUpdateClassTable.setInt(3, id);
				int result = 1 - statementUpdateClassTable.executeUpdate();
				statementUpdateClassTable.close();
				statementUpdateClassTable = null;
				return result;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	/**
	 * @return
	 * 0: Success 
	 * 1: Unknown perusal.
	 */
	public static int deleteClassPerusal(int class_id, int id) {
		try {
			if(!haveTable("class_" + class_id + "_perusal")) {
				return 1;
			} else {
				statementUpdateClassTable = connection.prepareStatement("DELETE FROM class_" + class_id + "_perusal WHERE id = ?");
				statementUpdateClassTable.setInt(1, id);
				int result = 1 - statementUpdateClassTable.executeUpdate();
				statementUpdateClassTable.close();
				statementUpdateClassTable = null;
				return result;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	/**
	 * =========================================================================================
	 */

	public static int getUserClassID(String username) {
		try {
			if(!haveUser(username)) {
				return 0;
			} else {
				ResultSet resultSet = getUser(username);
				resultSet.next();
				int class_id = resultSet.getInt("class_id");
				resultSet.close();
				return class_id;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static String getUserNickname(String username) {
		try {
			if(!haveUser(username)) {
				return null;
			} else {
				ResultSet resultSet = getUser(username);
				resultSet.next();
				String nickname = resultSet.getString("nickname");
				resultSet.close();
				return nickname;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getUserImageData(String username) {
		try {
			if(!haveUser(username)) {
				return null;
			} else {
				ResultSet resultSet = getUser(username);
				resultSet.next();
				String nickname = resultSet.getString("image_data");
				resultSet.close();
				return nickname;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getUserClassName(String username) {
		try {
			int class_id = getUserClassID(username);
			if(class_id == 0 || !haveClass(class_id)) {
				return null;
			} else {
				ResultSet resultSet = getClass(class_id);
				resultSet.next();
				String classname = resultSet.getString("classname");
				resultSet.close();
				return classname;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static LinkedList<DataNotice> getUserAllNotice(String username) {
		try {
			int class_id = getUserClassID(username);
			if(class_id == 0 || !haveClass(class_id)) {
				return null;
			} else {
				statementSelectClassTable = connection.prepareStatement("SELECT * FROM class_" + class_id + "_notice");
				ResultSet resultSet = statementSelectClassTable.executeQuery();
				LinkedList<DataNotice> data = new LinkedList<DataNotice>();
				while (resultSet.next()) {
					data.add(DataNotice.fromData(resultSet.getString("username"), getUserNickname(resultSet.getString("username")), resultSet.getString("title"), resultSet.getString("document"), resultSet.getTimestamp("timestamp"), resultSet.getInt("id")));
				}
				resultSet.close();
				return data;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static LinkedList<DataPerusal> getUserAllPerusal(String username) {
		try {
			int class_id = getUserClassID(username);
			if(class_id == 0 || !haveClass(class_id)) {
				return null;
			} else {
				statementSelectClassTable = connection.prepareStatement("SELECT * FROM class_" + class_id + "_perusal");
				ResultSet resultSet = statementSelectClassTable.executeQuery();
				LinkedList<DataPerusal> data = new LinkedList<DataPerusal>();
				while (resultSet.next()) {
					data.add(DataPerusal.fromData(resultSet.getString("username"), getUserNickname(resultSet.getString("username")), resultSet.getString("title"), resultSet.getString("document"), resultSet.getTimestamp("timestamp"), resultSet.getInt("max_choose"), resultSet.getString("options"), resultSet.getString("chooses"), resultSet.getString("comments"), resultSet.getInt("id")));
				}
				resultSet.close();
				return data;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static LinkedList<DataFile> getUserAllFile(String username) {
		try {
			int class_id = getUserClassID(username);
			if(class_id == 0 || !haveClass(class_id)) {
				return null;
			} else {
				statementSelectClassTable = connection.prepareStatement("SELECT * FROM class_" + class_id + "_file");
				ResultSet resultSet = statementSelectClassTable.executeQuery();
				LinkedList<DataFile> data = new LinkedList<DataFile>();
				while (resultSet.next()) {
					data.add(DataFile.fromData(resultSet.getString("username"), getUserNickname(resultSet.getString("username")), resultSet.getString("filename"), resultSet.getLong("size"), resultSet.getTimestamp("timestamp"), resultSet.getInt("id")));
				}
				resultSet.close();
				return data;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getFilePath(int class_id, int id) {
		try {
			if(class_id == 0 || !haveClass(class_id)) {
				return null;
			} else {
				statementSelectClassTable = connection.prepareStatement("SELECT * FROM class_" + class_id + "_file WHERE id = ?");
				statementSelectClassTable.setInt(1, id);
				ResultSet resultSet = statementSelectClassTable.executeQuery();
				String path = null;
				if (resultSet.next()) {
					path = resultSet.getString("path");
				}
				resultSet.close();
				return path;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static int getMD5FileCount(String md5Path) {
		int count = 0;
		try {
			statementSelectClassTable = connection.prepareStatement("SELECT * FROM class");
			ResultSet resultSet = statementSelectClassTable.executeQuery();
			while (resultSet.next()) {
				int class_id = resultSet.getInt("id");
				statementSelectClassTable = connection.prepareStatement("SELECT * FROM class_" + class_id + "_file");
				ResultSet resultSet_2 = statementSelectClassTable.executeQuery();
				while (resultSet_2.next()) {
					if(resultSet_2.getString("path").equals(md5Path)) {
						count++;
					}
				}
				resultSet_2.close();
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public static LinkedList<DataUser> getAllClassUser(String username) {
		try {
			int class_id = getUserClassID(username);
			if(class_id == 0 || !haveClass(class_id)) {
				return null;
			} else {
				statementSelectUserTable = connection.prepareStatement("SELECT * FROM user WHERE class_id = ?");
				statementSelectUserTable.setInt(1, class_id);
				ResultSet resultSet = statementSelectUserTable.executeQuery();
				LinkedList<DataUser> data = new LinkedList<DataUser>();
				while (resultSet.next()) {
					data.add(DataUser.fromData(resultSet.getString("username"), getUserNickname(resultSet.getString("username")), /*resultSet.getString("image_data")*/"", resultSet.getBoolean("authority")));
				}
				resultSet.close();
				return data;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static LinkedList<String> getAllUserInClass(int class_id) {
		try {
			if(class_id == 0 || !haveClass(class_id)) {
				return null;
			} else {
				statementSelectUserTable = connection.prepareStatement("SELECT * FROM user WHERE class_id = ?");
				statementSelectUserTable.setInt(1, class_id);
				ResultSet resultSet = statementSelectUserTable.executeQuery();
				LinkedList<String> data = new LinkedList<String>();
				while (resultSet.next()) {
					data.add(resultSet.getString("username"));
				}
				resultSet.close();
				return data;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static LinkedList<DataProfileImage> getAllUserProfileInClass(int class_id) {
		try {
			if(class_id == 0 || !haveClass(class_id)) {
				return null;
			} else {
				statementSelectUserTable = connection.prepareStatement("SELECT * FROM user WHERE class_id = ?");
				statementSelectUserTable.setInt(1, class_id);
				ResultSet resultSet = statementSelectUserTable.executeQuery();
				LinkedList<DataProfileImage> data = new LinkedList<DataProfileImage>();
				while (resultSet.next()) {
					data.add(DataProfileImage.fromData(resultSet.getString("username"), resultSet.getString("image_data")));
				}
				resultSet.close();
				return data;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static LinkedList<DataChat> getUserAllChat(String username) {
		try {
			int class_id = getUserClassID(username);
			if(class_id == 0 || !haveClass(class_id)) {
				return null;
			} else {
				statementSelectClassTable = connection.prepareStatement("SELECT * FROM class_" + class_id + "_chat");
				ResultSet resultSet = statementSelectClassTable.executeQuery();
				LinkedList<DataChat> data = new LinkedList<DataChat>();
				while (resultSet.next()) {
					data.add(DataChat.fromData(resultSet.getString("username"), getUserNickname(resultSet.getString("username")), resultSet.getString("document"), resultSet.getBoolean("withdraw"), resultSet.getTimestamp("timestamp"), resultSet.getInt("id")));
				}
				resultSet.close();
				return data;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static LinkedList<DataChat> getUserPrivateChat(String username, String username_to) {
		try {
			if(!haveUser(username) || !haveUser(username_to)) {
				return null;
			} else {
				statementSelectUserTable = connection.prepareStatement("SELECT * FROM user_" + username + "_chat WHERE username = ?");
				statementSelectUserTable.setString(1, username_to);
				ResultSet resultSet = statementSelectUserTable.executeQuery();
				LinkedList<DataChat> data = new LinkedList<DataChat>();
				while (resultSet.next()) {
					data.add(DataChat.fromData(username, getUserNickname(username), resultSet.getString("document"), resultSet.getBoolean("withdraw"), resultSet.getTimestamp("timestamp"), resultSet.getInt("id")));
				}
				resultSet.close();
				if(!username.equals(username_to)) {
					statementSelectUserTable = connection.prepareStatement("SELECT * FROM user_" + username_to + "_chat WHERE username = ?");
					statementSelectUserTable.setString(1, username);
					resultSet = statementSelectUserTable.executeQuery();
					while (resultSet.next()) {
						boolean add = false;
						DataChat temp = DataChat.fromData(username_to, getUserNickname(username_to), resultSet.getString("document"), resultSet.getBoolean("withdraw"), resultSet.getTimestamp("timestamp"), resultSet.getInt("id"));
						for (int i = 0; i < data.size(); i++) {
							if(data.get(i).timestamp.getTime() > temp.timestamp.getTime()) {
								data.add(i, temp);
								add = true;
								break;
							}
						}
						if(!add) data.add(temp);
					}
					resultSet.close();
				}
				return data;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean isAdmin(String username) {
		try {
			if(!haveUser(username)) {
				return false;
			} else {
				ResultSet resultSet = getUser(username);
				resultSet.next();
				boolean authority = resultSet.getBoolean("authority");
				resultSet.close();
				return authority;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static String getFileUsername(int class_id, int id) {
		try {
			if(class_id == 0 || !haveClass(class_id)) {
				return null;
			} else {
				statementSelectClassTable = connection.prepareStatement("SELECT * FROM class_" + class_id + "_file WHERE id = ?");
				statementSelectClassTable.setInt(1, id);
				ResultSet resultSet = statementSelectClassTable.executeQuery();
				String username = null;
				if (resultSet.next()) {
					username = resultSet.getString("username");
				}
				resultSet.close();
				return username;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getPerusalUsername(int class_id, int id) {
		try {
			if(class_id == 0 || !haveClass(class_id)) {
				return null;
			} else {
				statementSelectClassTable = connection.prepareStatement("SELECT * FROM class_" + class_id + "_perusal WHERE id = ?");
				statementSelectClassTable.setInt(1, id);
				ResultSet resultSet = statementSelectClassTable.executeQuery();
				String username = null;
				if (resultSet.next()) {
					username = resultSet.getString("username");
				}
				resultSet.close();
				return username;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getClassChatUsername(int class_id, int id) {
		try {
			if(class_id == 0 || !haveClass(class_id)) {
				return null;
			} else {
				statementSelectClassTable = connection.prepareStatement("SELECT * FROM class_" + class_id + "_chat WHERE id = ?");
				statementSelectClassTable.setInt(1, id);
				ResultSet resultSet = statementSelectClassTable.executeQuery();
				String username = null;
				if (resultSet.next()) {
					username = resultSet.getString("username");
				}
				resultSet.close();
				return username;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * =========================================================================================
	 */

	public static LinkedList<DataPaintAndGuess> getPaintAndGuess() {
		try {
			ResultSet resultSet = statementGetPaintAndGuess.executeQuery();
			LinkedList<DataPaintAndGuess> data = new LinkedList<DataPaintAndGuess>();
			while (resultSet.next()) {
				data.add(DataPaintAndGuess.fromData(resultSet.getString("item"), resultSet.getString("hint_1"), resultSet.getString("hint_2")));
			}
			resultSet.close();
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
