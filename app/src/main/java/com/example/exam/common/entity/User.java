package com.example.exam.common.entity;

    public class User {

        public static final String EMAIL ="email";
        public static final String PASSWORD ="password";
        public static final String FIRST_NAME ="firstName";
        public static final String LAST_NAME ="lastName";


        private static  User CurrentUser;

        public static User getCurrentUser() {
            if(CurrentUser==null)
                CurrentUser = new User();
            return CurrentUser;
        }
        private  long token;

        public long getToken() {
            return token;
        }

        public void setToken(long token) {
            this.token = token;
        }
    }

