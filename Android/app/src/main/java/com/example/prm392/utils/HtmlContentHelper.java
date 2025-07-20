package com.example.prm392.utils;

import android.text.Html;
import android.text.Spanned;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.text.style.ForegroundColorSpan;
import android.graphics.Color;
import android.graphics.Typeface;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlContentHelper {
    
    /**
     * Parse HTML content và tạo Spanned text với styling tùy chỉnh
     */
    public static Spanned parseHtmlContent(String htmlContent) {
        if (htmlContent == null || htmlContent.isEmpty()) {
            return Html.fromHtml("");
        }
        
        // Xử lý các thẻ HTML cơ bản
        String processedContent = htmlContent;
        
        // Thay thế các thẻ HTML bằng styling Android
        processedContent = processHtmlTags(processedContent);
        
        // Sử dụng Html.fromHtml() cho các thẻ còn lại
        Spanned spanned;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(processedContent, Html.FROM_HTML_MODE_COMPACT);
        } else {
            spanned = Html.fromHtml(processedContent);
        }
        
        return spanned;
    }
    
    /**
     * Xử lý các thẻ HTML cụ thể
     */
    private static String processHtmlTags(String content) {
        // Xử lý thẻ h1, h2, h3, h4, h5, h6
        content = content.replaceAll("<h1[^>]*>(.*?)</h1>", "<b><font color='#1A1A1A' size='24'>$1</font></b>");
        content = content.replaceAll("<h2[^>]*>(.*?)</h2>", "<b><font color='#1A1A1A' size='20'>$1</font></b>");
        content = content.replaceAll("<h3[^>]*>(.*?)</h3>", "<b><font color='#1A1A1A' size='18'>$1</font></b>");
        content = content.replaceAll("<h4[^>]*>(.*?)</h4>", "<b><font color='#1A1A1A' size='16'>$1</font></b>");
        content = content.replaceAll("<h5[^>]*>(.*?)</h5>", "<b><font color='#1A1A1A' size='14'>$1</font></b>");
        content = content.replaceAll("<h6[^>]*>(.*?)</h6>", "<b><font color='#1A1A1A' size='12'>$1</font></b>");
        
        // Xử lý thẻ p
        content = content.replaceAll("<p[^>]*>(.*?)</p>", "<font color='#333333' size='16'>$1</font><br/>");
        
        // Xử lý thẻ strong và b
        content = content.replaceAll("<strong[^>]*>(.*?)</strong>", "<b>$1</b>");
        content = content.replaceAll("<b[^>]*>(.*?)</b>", "<b>$1</b>");
        
        // Xử lý thẻ em và i
        content = content.replaceAll("<em[^>]*>(.*?)</em>", "<i>$1</i>");
        content = content.replaceAll("<i[^>]*>(.*?)</i>", "<i>$1</i>");
        
        // Xử lý thẻ u
        content = content.replaceAll("<u[^>]*>(.*?)</u>", "<u>$1</u>");
        
        // Xử lý thẻ br
        content = content.replaceAll("<br[^>]*>", "<br/>");
        
        // Xử lý thẻ ul và li
        content = content.replaceAll("<ul[^>]*>", "");
        content = content.replaceAll("</ul>", "");
        content = content.replaceAll("<li[^>]*>(.*?)</li>", "• $1<br/>");
        
        // Xử lý thẻ ol và li
        content = content.replaceAll("<ol[^>]*>", "");
        content = content.replaceAll("</ol>", "");
        content = content.replaceAll("<li[^>]*>(.*?)</li>", "1. $1<br/>");
        
        return content;
    }
    
    /**
     * Tạo Spanned text với styling tùy chỉnh cho từng loại thẻ
     */
    public static Spanned createStyledContent(String htmlContent) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        
        // Parse HTML content
        String[] lines = htmlContent.split("\n");
        int currentPosition = 0;
        
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                builder.append("\n");
                currentPosition++;
                continue;
            }
            
            // Xử lý thẻ h2
            if (line.matches("<h2[^>]*>.*?</h2>")) {
                String text = extractTextFromTag(line, "h2");
                int start = builder.length();
                builder.append(text);
                int end = builder.length();
                
                // Apply styling cho h2
                builder.setSpan(new StyleSpan(Typeface.BOLD), start, end, 0);
                builder.setSpan(new ForegroundColorSpan(Color.parseColor("#1A1A1A")), start, end, 0);
                
                builder.append("\n\n");
                currentPosition = builder.length();
            }
            // Xử lý thẻ p
            else if (line.matches("<p[^>]*>.*?</p>")) {
                String text = extractTextFromTag(line, "p");
                int start = builder.length();
                builder.append(text);
                int end = builder.length();
                
                // Apply styling cho p
                builder.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), start, end, 0);
                
                builder.append("\n\n");
                currentPosition = builder.length();
            }
            // Xử lý text thường
            else {
                builder.append(line);
                builder.append("\n");
                currentPosition = builder.length();
            }
        }
        
        return builder;
    }
    
    /**
     * Trích xuất text từ thẻ HTML
     */
    private static String extractTextFromTag(String htmlLine, String tagName) {
        Pattern pattern = Pattern.compile("<" + tagName + "[^>]*>(.*?)</" + tagName + ">");
        Matcher matcher = pattern.matcher(htmlLine);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return htmlLine;
    }
    
    /**
     * Kiểm tra xem content có chứa HTML tags không
     */
    public static boolean containsHtmlTags(String content) {
        if (content == null) return false;
        return content.contains("<") && content.contains(">");
    }
} 