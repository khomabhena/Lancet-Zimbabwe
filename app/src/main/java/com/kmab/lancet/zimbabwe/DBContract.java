package com.kmab.lancet.zimbabwe;

public final class DBContract {

    public DBContract() {
    }


    static abstract class Forms {
        static final String TABLE_NAME = "table_twelve";
        static final String ID = "_id";

        static final String TIMESTAMP = "local_uid";
        static final String IS_SEEN = "col_1";
        static final String IS_MALE = "col_2";

        static final String KEY = "col_3";
        static final String UID = "col_4";
        static final String PATIENT_NAME = "col_5";
        static final String PATIENT_SURNAME = "col_6";
        static final String RELATIONSHIP_TO_MEMBER = "col_7";
        static final String MEMBER_NAME = "col_8";
        static final String MEMBER_SURNAME = "col_9";
        static final String MEDICAL_AID = "col_10";
        static final String EMAIL = "col_11";
        static final String SUFFIX = "col_12";
        static final String NO = "col_13";
        static final String ADDRESS = "col_14";
        static final String EMPLOYER = "col_15";
        static final String PHONE = "col_16";
        static final String SPECIMEN_TYPE = "col_17";
        static final String MEDICAL_LINK = "col_18";
        static final String FORM_LINK = "col_19";
        static final String PATIENT_DOB = "col_20";
        static final String TISSUE_SAMPLE = "col_20TissueSample";
    }

    static abstract class Messages {
        static final String TABLE_NAME = "table_thirteen";
        static final String ID = "_id";
        static final String LOCAL_UID = "local_uid";
        static final String KEY = "col_1";
        static final String UID = "col_2";
        static final String MESSAGE = "col_3";
        static final String IS_SEEN = "col_4";
        static final String TIMESTAMP = "col_6";
        static final String QUESTION = "col_5";
        static final String LANGUAGE = "col_7";
        static final String IS_REPLIED = "col_8";
        static final String COl_9 = "col_9";
        static final String COl_10 = "col_10";
        static final String COl_11 = "col_11";
        static final String COl_12 = "col_12";
        static final String COl_13 = "col_13";
        static final String COl_14 = "col_14";
        static final String COl_15 = "col_15";
        static final String COl_16 = "col_16";
        static final String COl_17 = "col_17";
        static final String COl_18 = "col_18";
        static final String COl_19 = "col_19";
        static final String COl_20 = "col_20";
    }

    static abstract class Locations {
        static final String TABLE_NAME = "table_questions";
        static final String ID = "_id";
        static final String LOCAL_UID = "local_uid";
        static final String KEY = "col_1";
        static final String BUILDING = "col_2";
        static final String ADDRESS = "col_3";
        static final String CITY = "col_4";
        static final String QUESTION = "col_5";
        static final String TIMESTAMP = "col_6";
        static final String LANGUAGE = "col_7";
        static final String COl_8 = "col_8";
        static final String COl_9 = "col_9";
        static final String COl_10 = "col_10";
        static final String COl_11 = "col_11";
        static final String COl_12 = "col_12";
        static final String COl_13 = "col_13";
        static final String COl_14 = "col_14";
        static final String COl_15 = "col_15";
        static final String COl_16 = "col_16";
        static final String COl_17 = "col_17";
        static final String COl_18 = "col_18";
        static final String COl_19 = "col_19";
        static final String COl_20 = "col_20";
    }

    static abstract class Responses {
        static final String TABLE_NAME = "table_responses_prep";
        static final String ID = "_id";
        static final String LOCAL_UID = "local_uid";
        static final String KEY = "col_1";
        static final String PARENT_KEY = "col_2";
        static final String NAME = "col_3";
        static final String RESPONSE = "col_4";
        static final String ENABLED = "col_5";
        static final String TIMESTAMP = "col_6";
        static final String LANGUAGE = "col_7";
        static final String COl_8 = "col_8";
        static final String COl_9 = "col_9";
        static final String COl_10 = "col_10";
        static final String COl_11 = "col_11";
        static final String COl_12 = "col_12";
        static final String COl_13 = "col_13";
        static final String COl_14 = "col_14";
        static final String COl_15 = "col_15";
        static final String COl_16 = "col_16";
        static final String COl_17 = "col_17";
        static final String COl_18 = "col_18";
        static final String COl_19 = "col_19";
        static final String COl_20 = "col_20";
    }

    static abstract class Actions {
        static final String TABLE_NAME = "table_actions_prep";
        static final String ID = "_id";
        static final String LOCAL_UID = "local_uid";
        static final String KEY = "col_1";
        static final String PARENT_KEY = "col_2";
        static final String NAME = "col_3";
        static final String ACTION = "col_4";
        static final String ENABLED = "col_5";
        static final String TIMESTAMP = "col_6";
        static final String LANGUAGE = "col_7";
        static final String COl_8 = "col_8";
        static final String COl_9 = "col_9";
        static final String COl_10 = "col_10";
        static final String COl_11 = "col_11";
        static final String COl_12 = "col_12";
        static final String COl_13 = "col_13";
        static final String COl_14 = "col_14";
        static final String COl_15 = "col_15";
        static final String COl_16 = "col_16";
        static final String COl_17 = "col_17";
        static final String COl_18 = "col_18";
        static final String COl_19 = "col_19";
        static final String COl_20 = "col_20";
    }

    static abstract class Keywords {
        static final String TABLE_NAME = "table_chats";
        static final String ID = "_id";
        static final String LOCAL_UID = "local_uid";
        static final String KEY = "col_1";
        static final String NAME = "col_3";
        static final String TYPE = "col_2";
        static final String ENABLED = "col_4";
        static final String QUESTION = "col_5";
        static final String TIMESTAMP = "col_6";
        static final String LANGUAGE = "col_7";
        static final String COl_8 = "col_8";
        static final String COl_9 = "col_9";
        static final String COl_10 = "col_10";
        static final String COl_11 = "col_11";
        static final String COl_12 = "col_12";
        static final String COl_13 = "col_13";
        static final String COl_14 = "col_14";
        static final String COl_15 = "col_15";
        static final String COl_16 = "col_16";
        static final String COl_17 = "col_17";
        static final String COl_18 = "col_18";
        static final String COl_19 = "col_19";
        static final String COl_20 = "col_20";
    }

}