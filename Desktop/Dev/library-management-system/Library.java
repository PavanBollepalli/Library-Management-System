import java.sql.*;

public class Library {

    public void addBook(Book book) {
        String query = "INSERT INTO books (book_id, title, author, is_issued) VALUES (?, ?, ?, false)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, book.getBookId());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getAuthor());
            ps.executeUpdate();
            System.out.println("Book added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding book: " + e.getMessage());
        }
    }

    public void displayBooks() {
        String query = "SELECT * FROM books ORDER BY book_id";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            System.out.println("Book ID | Title | Author | Status");
            while (rs.next()) {
                String status = rs.getBoolean("is_issued") ? "Issued" : "Available";
                System.out.println(rs.getString("book_id") + " | " +
                                   rs.getString("title") + " | " +
                                   rs.getString("author") + " | " + status);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching books: " + e.getMessage());
        }
    }

    public void issueBook(String id) {
        String query = "UPDATE books SET is_issued = true WHERE book_id = ? AND is_issued = false";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println("Book issued successfully!");
            else System.out.println("Book not available or already issued.");
        } catch (SQLException e) {
            System.out.println("Error issuing book: " + e.getMessage());
        }
    }

    public void returnBook(String id) {
        String query = "UPDATE books SET is_issued = false WHERE book_id = ? AND is_issued = true";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) System.out.println("Book returned successfully!");
            else System.out.println("Invalid Book ID or book not issued.");
        } catch (SQLException e) {
            System.out.println("Error returning book: " + e.getMessage());
        }
    }
}
