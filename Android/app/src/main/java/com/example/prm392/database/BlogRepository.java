package com.example.prm392.database;

import android.util.Log;
import com.example.prm392.model.BlogModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BlogRepository {

    public static List<BlogModel> getAllBlogs() {
        List<BlogModel> blogs = new ArrayList<>();
        Connection conn = DBHelper.getConnection();

        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM NewsArticles WHERE IsActive = 1");

                while (rs.next()) {
                    BlogModel blog = new BlogModel(
                            rs.getInt("Id"),
                            rs.getString("Title"),
                            rs.getString("Content"),
                            rs.getString("ImageUrl"),
                            rs.getString("PublishDate"),
                            rs.getString("ReadTime"),
                            rs.getInt("Views"),
                            rs.getInt("Likes"),
                            rs.getInt("Comments")
                    );
                    blogs.add(blog);
                }

                rs.close();
                conn.close();
                Log.d("BLOG_DB", "Loaded: " + blogs.size());
            } catch (Exception e) {
                Log.e("BLOG_DB", "Error: " + e.getMessage());
            }
        }
        return blogs;
    }

    public static List<BlogModel> searchBlogsByTitle(String searchQuery) {
        List<BlogModel> blogs = new ArrayList<>();
        Connection conn = DBHelper.getConnection();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                String query = "SELECT * FROM NewsArticles WHERE IsActive = 1 AND Title COLLATE Latin1_General_CI_AI LIKE N'%" + searchQuery + "%'";
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    BlogModel blog = new BlogModel(
                            rs.getInt("Id"),
                            rs.getString("Title"),
                            rs.getString("Content"),
                            rs.getString("ImageUrl"),
                            rs.getString("PublishDate"),
                            rs.getString("ReadTime"),
                            rs.getInt("Views"),
                            rs.getInt("Likes"),
                            rs.getInt("Comments")
                    );
                    blogs.add(blog);
                }

                rs.close();
                conn.close();
            } catch (Exception e) {
                Log.e("BLOG_DB", "Error searching blogs: " + e.getMessage());
            }
        }
        return blogs;
    }

    public static boolean updateBlogViews(int blogId) {
        Connection conn = DBHelper.getConnection();
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                String query = "UPDATE NewsArticles SET Views = Views + 1 WHERE Id = " + blogId;
                int rowsAffected = stmt.executeUpdate(query);
                conn.close();
                
                if (rowsAffected > 0) {
                    Log.d("BLOG_DB", "Successfully updated views for blog ID: " + blogId);
                    return true;
                } else {
                    Log.w("BLOG_DB", "No rows affected when updating views for blog ID: " + blogId);
                    return false;
                }
            } catch (Exception e) {
                Log.e("BLOG_DB", "Error updating blog views: " + e.getMessage());
                return false;
            }
        }
        return false;
    }
}
