package com.build2gether.fx.Firebase;

/**
 * Created by abhilashnair on 3/7/16.
 */
public enum FirebaseUtil {

    URL {
        public String toString() {
            return "https://fx-barter.firebaseio.com";
        }
    },
    USERS{
        public String toString() {
            return "https://fx-barter.firebaseio.com/users";
        }
    },
    INVENTORY{
        public String toString() {
            return "https://fx-barter.firebaseio.com/inventory";
        }
    },
    MESSENGER{
        public String toString() {
            return "https://fx-barter.firebaseio.com/Messenger";
        }
    },
    CHATROOM{
        public String toString() {
            return "https://fx-barter.firebaseio.com/Messenger/Chatroom";
        }
    },
    MESSENGERUSER{
        public String toString() { return "https://fx-barter.firebaseio.com/Messenger/Users"; }
    },
    NOTIFICATION {
        public String toString() { return "https://fx-barter.firebaseio.com/Notification"; }
    }
    ;


};
