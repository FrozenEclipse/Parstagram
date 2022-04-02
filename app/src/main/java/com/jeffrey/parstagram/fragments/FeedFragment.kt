package com.jeffrey.parstagram.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jeffrey.parstagram.MainActivity
import com.jeffrey.parstagram.Post
import com.jeffrey.parstagram.PostAdapter
import com.jeffrey.parstagram.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery


open class FeedFragment : Fragment() {

    lateinit var postsRecyclerView: RecyclerView

    lateinit var adapter: PostAdapter

    lateinit var swipeContainer: SwipeRefreshLayout

    var allPosts: MutableList<Post> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeContainer = view.findViewById(R.id.swipeContainer)
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light);
        swipeContainer.setOnRefreshListener {
            Log.i(TAG, "Refreshing timeline")
            queryPosts()
        }

        postsRecyclerView = view.findViewById(R.id.postRecyclerView)

        adapter = PostAdapter(requireContext(), allPosts as ArrayList<Post>)
        postsRecyclerView.adapter = adapter

        postsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        queryPosts()
    }
    open fun queryPosts(){
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)//.setLimit(1) <- works too
        query.limit = 20
        query.include(Post.KEY_USER) // star
        query.addDescendingOrder("createdAt")
        query.findInBackground(object: FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null) {
                    Log.e(MainActivity.TAG, "Error fetching posts")
                } else {
                    if (posts != null){
                        adapter.clear()
                        for (post in posts){
                            Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser()?.username)
                        }
                        allPosts.addAll(posts)
                        adapter.notifyDataSetChanged()
                        swipeContainer.setRefreshing(false)
                        Toast.makeText(requireContext(), "Successfully refreshed!", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        })
    }

    companion object{
        const val TAG = "FeedFragment"
    }

}