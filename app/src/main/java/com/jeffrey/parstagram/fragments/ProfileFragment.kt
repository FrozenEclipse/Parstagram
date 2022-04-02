package com.jeffrey.parstagram.fragments

import android.util.Log
import com.jeffrey.parstagram.MainActivity
import com.jeffrey.parstagram.Post
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

class ProfileFragment: FeedFragment() {

    override fun queryPosts(){
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)//.setLimit(1) <- works too
//        query.limit = 20
        query.include(Post.KEY_USER) // star
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser())
        query.addDescendingOrder("createdAt")
        query.findInBackground(object: FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null) {
                    Log.e(MainActivity.TAG, "Error fetching posts")
                } else {
                    if (posts != null){
                        for (post in posts){
                            Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser()?.username)
                        }
                        allPosts.addAll(posts)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

        })
    }
}