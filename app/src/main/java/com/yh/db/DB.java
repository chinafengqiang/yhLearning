package com.yh.db;

/**
 *本地数据库
 */
public interface DB {
	
	public static final int DATABASE_VERSION = 1;
	
	public static final String dbPath = android.os.Environment.getExternalStorageDirectory().getPath() + "/yh";
	public static final String DATABASE_NAME = dbPath + "/" + "yh.db";
	
	
	/**
	 *本地表结构
	 */
	public interface TABLES {

		/**
		 * 消息
		 */
		public interface MESSAGE {
			public static final String TABLENAME = "tbl_message";

			public interface FIELDS {
				public static final String ID = "id";
				public static final String TITLE = "title";//
				public static final String MSG = "msg";
				public static final String STATUS = "status";
				public static final String R_TIME = "r_time";
				public static final String USER_ID = "user_id";
				public static final String EXT1 = "ext1";
				public static final String EXT2 = "ext2";
			}

			public interface SQL {
				public static final String CREATE = "create table if not exists tbl_message(id integer PRIMARY KEY ,title varchar(128),msg varchar(1024),status Integer,r_time varchar(50),user_id Integer,ext1 varchar(100),ext2 varchar(100))";
				public static final String DROP = "drop table if exists tbl_message";
				public static final String INSERT = "insert into tbl_message(title,msg,r_time,status,user_id,ext1,ext2) values('%s','%s','%s',s%,s%,'%s','%s') ";// 插入
				public static final String SELECT = "select * from tbl_message where %s";// 查询
				public static final String DELETE = "delete FROM tbl_message where %s";
				public static final String SELECT_ALL = "select * from tbl_message";
				public static final String SELECT_COUNT = "select count(*) from tbl_message where %s";
			}
		}


		/**
		 * 课程表
		 */
		public interface LESSON {
			public static final String TABLENAME = "tbl_lesson";

			public interface FIELDS {
				public static final String LESSON_ID = "l_id";
				public static final String LESSON_NUM = "l_num";
				public static final String LESSON_TIME = "l_time";
				public static final String WEEK_ONE_LESSON = "l_week_one";
				public static final String WEEK_TWO_LESSON = "l_week_two";
				public static final String WEEK_THREE_LESSON = "l_week_three";
				public static final String WEEK_FOUR_LESSON = "l_week_four";
				public static final String WEEK_FIVE_LESSON = "l_week_five";
				public static final String WEEK_SIX_LESSON = "l_week_six";
				public static final String WEEK_SEVEN_LESSON = "l_week_seven";
			}

			public interface SQL {
				public static final String CREATE = "create table if not exists tbl_lesson(l_id integer,l_num integer,l_time varchar(50),l_week_one varchar(32),l_week_two varchar(32),l_week_three varchar(32),l_week_four varchar(32),l_week_five varchar(32),l_week_six varchar(32),l_week_seven varchar(32))";
				public static final String DROP = "drop table if exists tbl_lesson";
				public static final String INSERT = "insert into tbl_lesson(l_id,l_num,l_time,l_week_one,l_week_two,l_week_three,l_week_four,l_week_five) values(s%,s%,'%s','%s','%s','%s','%s','%s') ";// 插入
				public static final String SELECT = "select * from tbl_lesson where %s";// 查询
				public static final String DELETE = "delete FROM tbl_lesson where %s";
				public static final String SELECT_ALL = "select * from tbl_lesson";
				public static final String SELECT_COUNT = "select count(*) from tbl_lesson where %s";
				public static final String DELETE_ALL = "delete FROM tbl_lesson";
			}
		}
	}

}