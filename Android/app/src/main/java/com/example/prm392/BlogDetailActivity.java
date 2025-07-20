package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.prm392.model.BlogModel;
import com.example.prm392.utils.HtmlContentHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlogDetailActivity extends AppCompatActivity {

    private ImageView imgBlogDetail;
    private TextView tvTitle, tvContent, tvDate, tvReadTime, tvViews, tvLikes, tvComments;
    private LinearLayout llHtmlContent;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);

        // Khởi tạo toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Blog Detail");
        }

        // Khởi tạo bottom navigation
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    // Navigate to home
                    Intent intent = new Intent(BlogDetailActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_bookings) {
                    // Navigate to booking
                    Intent intent = new Intent(BlogDetailActivity.this, BookingActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_chat) {
                    // Navigate to chat (you can implement this later)
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    // Navigate to profile
                    Intent intent = new Intent(BlogDetailActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        // Ánh xạ view
        imgBlogDetail = findViewById(R.id.imgBlogDetail);
        tvTitle = findViewById(R.id.tvBlogTitle);
        tvContent = findViewById(R.id.tvBlogContent);
        llHtmlContent = findViewById(R.id.llHtmlContent);
        tvDate = findViewById(R.id.tvPublishDate);
        tvReadTime = findViewById(R.id.tvReadTime);
        tvViews = findViewById(R.id.tvViews);
        tvLikes = findViewById(R.id.tvLikes);
        tvComments = findViewById(R.id.tvComments);

        // Set tab blog được chọn giống trang Blog
        bottomNavigation.setSelectedItemId(R.id.nav_chat);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        BlogModel blog = (BlogModel) intent.getSerializableExtra("BlogData");

        // Hiển thị dữ liệu lên UI
        if (blog != null) {
            tvTitle.setText(blog.getTitle());
            
            // Xử lý HTML content
            String htmlContent = blog.getContent();
            if (htmlContent != null && !htmlContent.isEmpty()) {
                // Luôn hiển thị nội dung, dù có HTML tags hay không
                displayContent(htmlContent);
            } else {
                // Nếu không có nội dung
                tvContent.setVisibility(View.VISIBLE);
                llHtmlContent.setVisibility(View.GONE);
                tvContent.setText("No content available");
            }
            
            tvDate.setText(blog.getPublishDate());
            tvReadTime.setText(blog.getReadTime());
            tvViews.setText(String.valueOf(blog.getViews()));
            tvLikes.setText(String.valueOf(blog.getLikes()));
            tvComments.setText(String.valueOf(blog.getComments()));

            // Load ảnh bằng Glide
            Glide.with(this)
                    .load(blog.getImageUrl())
                    .placeholder(R.drawable.sample_blog_image)
                    .error(R.drawable.sample_blog_image)
                    .into(imgBlogDetail);
        }
    }

    /**
     * Hiển thị nội dung - sử dụng TextView cũ hoặc layout tùy chỉnh
     */
    private void displayContent(String content) {
        // Debug: In ra nội dung để kiểm tra
        System.out.println("DEBUG - Content: " + content);
        
        // Kiểm tra xem có HTML tags không
        boolean hasHtmlTags = content.contains("<") && content.contains(">");
        System.out.println("DEBUG - Has HTML tags: " + hasHtmlTags);
        
        if (hasHtmlTags) {
            // Sử dụng layout tùy chỉnh cho HTML
            tvContent.setVisibility(View.GONE);
            llHtmlContent.setVisibility(View.VISIBLE);
            displayHtmlContent(content);
        } else {
            // Sử dụng TextView cũ cho text thường
            tvContent.setVisibility(View.VISIBLE);
            llHtmlContent.setVisibility(View.GONE);
            tvContent.setText(content);
        }
    }

    /**
     * Hiển thị nội dung HTML với layout tùy chỉnh
     */
    private void displayHtmlContent(String htmlContent) {
        llHtmlContent.removeAllViews();
        
        // Debug: In ra HTML content
        System.out.println("DEBUG - HTML Content: " + htmlContent);
        
        // Thử parse theo từng thẻ HTML thay vì từng dòng
        parseHtmlTags(htmlContent);
        
        System.out.println("DEBUG - Total views added: " + llHtmlContent.getChildCount());
        
        // Nếu không có view nào được thêm, hiển thị text thường
        if (llHtmlContent.getChildCount() == 0) {
            System.out.println("DEBUG - No views added, falling back to TextView");
            tvContent.setVisibility(View.VISIBLE);
            llHtmlContent.setVisibility(View.GONE);
            tvContent.setText(htmlContent);
        }
    }

    /**
     * Parse HTML tags trong content
     */
    private void parseHtmlTags(String htmlContent) {
        // Tìm tất cả các thẻ h2
        parseTag(htmlContent, "h2");
        
        // Tìm tất cả các thẻ h3
        parseTag(htmlContent, "h3");
        
        // Tìm tất cả các thẻ p
        parseTag(htmlContent, "p");
    }

    /**
     * Parse một loại thẻ cụ thể
     */
    private void parseTag(String htmlContent, String tagName) {
        String openTag = "<" + tagName + ">";
        String closeTag = "</" + tagName + ">";
        
        int startIndex = 0;
        while (true) {
            int tagStart = htmlContent.indexOf(openTag, startIndex);
            if (tagStart == -1) break;
            
            int tagEnd = htmlContent.indexOf(closeTag, tagStart);
            if (tagEnd == -1) break;
            
            // Trích xuất nội dung bên trong thẻ
            int contentStart = tagStart + openTag.length();
            String content = htmlContent.substring(contentStart, tagEnd).trim();
            
            System.out.println("DEBUG - Found " + tagName + " tag with content: " + content);
            
            // Tạo TextView tương ứng
            if (tagName.equals("h2") || tagName.equals("h3")) {
                TextView headingView = createHeadingView(content, tagName);
                llHtmlContent.addView(headingView);
            } else if (tagName.equals("p")) {
                TextView paragraphView = createParagraphView(content);
                llHtmlContent.addView(paragraphView);
            }
            
            startIndex = tagEnd + closeTag.length();
        }
    }

    /**
     * Tạo TextView cho heading
     */
    private TextView createHeadingView(String text, String tagName) {
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        
        textView.setText(text);
        
        // Styling cho heading
        if (tagName.equals("h2")) {
            textView.setTextSize(20);
            textView.setTextColor(getResources().getColor(android.R.color.black));
            textView.setTypeface(null, android.graphics.Typeface.BOLD);
            textView.setPadding(0, 24, 0, 16);
        } else if (tagName.equals("h3")) {
            textView.setTextSize(18);
            textView.setTextColor(getResources().getColor(android.R.color.black));
            textView.setTypeface(null, android.graphics.Typeface.BOLD);
            textView.setPadding(0, 20, 0, 12);
        }
        
        return textView;
    }

    /**
     * Tạo TextView cho paragraph
     */
    private TextView createParagraphView(String text) {
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        
        textView.setText(text);
        
        // Styling cho paragraph
        textView.setTextSize(16);
        textView.setTextColor(getResources().getColor(android.R.color.darker_gray));
        textView.setLineSpacing(6, 1.2f);
        textView.setPadding(0, 8, 0, 8);
        
        return textView;
    }

    /**
     * Tạo TextView cho text thường
     */
    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        
        textView.setText(text);
        textView.setTextSize(16);
        textView.setTextColor(getResources().getColor(android.R.color.darker_gray));
        textView.setLineSpacing(6, 1.2f);
        textView.setPadding(0, 4, 0, 4);
        
        return textView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
