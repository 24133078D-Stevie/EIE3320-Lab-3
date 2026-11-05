/**
 * LAM King Shan (24131593D)
 * LAU Ching Hoi (24133078D)
 */

public class Book {
    private String isbn;
    private String title;
    private boolean available;
    
    public Book(String isbn, String title, boolean available) {
        this.isbn = isbn;
        this.title = title;
        this.available = available;
    }
    
    public String getISBN(){
        return isbn;
    }
    
    public void setISBN(String isbn){
        this.isbn = isbn;
    }
    
    public String getTitle(){
        return title;
    }
    
    public void setTitle(String title){
        this.title = title;
    }
    
    public boolean getAvailable(){
        return available;
    }
    
    public void setAvailable(boolean available){
        this.available = available;
    }
}