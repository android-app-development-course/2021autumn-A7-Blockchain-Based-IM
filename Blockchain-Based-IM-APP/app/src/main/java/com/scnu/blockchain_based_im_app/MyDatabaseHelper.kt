package com.scnu.blockchain_based_im_app

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabaseHelper(val context: Context, name: String, version: Int):
    SQLiteOpenHelper(context, name, null, version) {

    private val createUser = "create table user(" +
            "id text primary key," +
            "password text," +
            "name text," +
            "profile_photo blob)"

    private val createFriendship = "create table friendship(" +
            "ownerID text, " +
            "friendID text, " +
            "note text, " +
            "primary key(ownerID, friendID))"

    private val createFriendRequest = "create table friend_request(" +
            "applicantID text, " +
            "respondentID text, " +
            "primary key(applicantID, respondentID))"

    private val createChat = "create table chat(" +
            "ownerID text, " +
            "friendID text, " +
            "lastMessage text, " +
            "lastTime text," +
            "stickOnTop integer, " +
            "msgIgnore integer, " +
            "primary key(ownerID, friendID))"

    private val createMsgContent = "create table msg_content(" +
            "ownerID text, " +
            "friendID text, " +
            "content text, " +
            "msgType integer, " +
            "sendTime text, " +
            "primary key(ownerID, friendID, sendTime))"

    override fun onCreate(db: SQLiteDatabase?) {
        //版本1中新建的表
        db?.execSQL(createUser)
        db?.execSQL(createFriendship)
        db?.execSQL(createFriendRequest)

        //版本2中新建的表
        db?.execSQL(createChat)
        db?.execSQL(createMsgContent)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion <= 1) {
            db?.execSQL(createChat)
            db?.execSQL(createMsgContent)
        }
    }
}