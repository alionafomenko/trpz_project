package org.example.crawler_api.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.example.crawler_api.model.Content;
import org.springframework.stereotype.Repository;

@Repository
public class ContentRepository {

    private final String url = "jdbc:postgresql://localhost:5432/alionadb"; // Замените на URL вашей БД
    private final String user = "aliona"; // Замените на имя пользователя вашей БД
    private final String password = "aliona55"; // Замените на пароль вашей БД

    public List<Content> getSearchContent(String searchTerm) {
        List<Content> contentList = new ArrayList<>();

        String query = "SELECT * FROM trpz.get_search_content(?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, searchTerm);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Content content = new Content();
                    content.setSiteUrl(rs.getString("site_url"));
                    content.setSiteTitle(rs.getString("site_title"));
                    content.setDocUrl(rs.getString("doc_url"));
                    content.setDocTitle(rs.getString("doc_title"));
                    content.setContent(rs.getString("content"));

                    contentList.add(content);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contentList;
    }
}
