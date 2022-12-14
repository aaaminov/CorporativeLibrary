package com.aminov.corporativelibrary.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aminov.corporativelibrary.models.Author;
import com.aminov.corporativelibrary.models.Book;
import com.aminov.corporativelibrary.models.BookType;
import com.aminov.corporativelibrary.models.Comment;
import com.aminov.corporativelibrary.models.Genre;
import com.aminov.corporativelibrary.models.Library;
import com.aminov.corporativelibrary.models.User;
import com.aminov.corporativelibrary.models.UserBook;
import com.aminov.corporativelibrary.repositories.AuthorRepository;
import com.aminov.corporativelibrary.repositories.BookRepository;
import com.aminov.corporativelibrary.repositories.BookTypeRepository;
import com.aminov.corporativelibrary.repositories.CommentRepository;
import com.aminov.corporativelibrary.repositories.GenreRepository;
import com.aminov.corporativelibrary.repositories.LibraryRepository;
import com.aminov.corporativelibrary.repositories.UserBookRepository;
import com.aminov.corporativelibrary.repositories.UserRepository;

@Controller
@RequestMapping("/book")
public class BookController {

    private final BookRepository repository;
    
    private final AuthorRepository authorRepository;
    private final BookTypeRepository bookTypeRepository;
    private final CommentRepository commentRepository;
    private final GenreRepository genreRepository;
    private final LibraryRepository libraryRepository;
    private final UserRepository userRepository;
    private final UserBookRepository userBookRepository;

    public BookController(BookRepository repository, AuthorRepository authorRepository, 
        BookTypeRepository bookTypeRepository, CommentRepository commentRepository, 
        GenreRepository genreRepository, LibraryRepository libraryRepository, 
        UserRepository userRepository, UserBookRepository userBookRepository) {
        this.repository = repository;
        this.authorRepository = authorRepository;
        this.bookTypeRepository = bookTypeRepository;
        this.commentRepository = commentRepository;
        this.genreRepository = genreRepository;
        this.libraryRepository = libraryRepository;
        this.userRepository = userRepository;
        this.userBookRepository = userBookRepository;
    }

    // ?????????????????? ???????????????? ????????????????????????
    private User getCurrentUser() {
        String currentPrincipalName = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByLogin(currentPrincipalName);
    }

    @GetMapping("")
    public String home() {
        return "redirect:/book/search";
    }
    

    // ?????????????????? ???????? ????????
    @GetMapping("/search")
    public String search(Model model, 
        @RequestParam(value = "title", required = false, defaultValue = "") String title, 
        @RequestParam(value = "author_id", required = false) List<Long> author_id,
        @RequestParam(value = "genre_id", required = false, defaultValue = "0") Long genre_id) {
        List<Book> books = null;
        // System.out.format("LOLOL "+ String.valueOf(genre_id));
        if (title != "" && author_id != null && genre_id != 0){
            books = repository.findByTitleAndAuthorsAndGenre(title, author_id, genre_id);
        } 
        else if (title != "" && author_id == null && genre_id == 0){
            books = repository.findByTitle(title);
        } 
        else if (title == "" && author_id != null && genre_id == 0){
            books = repository.findByAuthor(author_id);
        } 
        else if (title == "" && author_id == null && genre_id != 0){
            books = repository.findByGenre(genre_id);
        } 
        else if (title != "" && author_id != null && genre_id == 0) {
            books = repository.findByTitleAndAuthors(title, author_id);
        }
        else if (title != "" && author_id == null && genre_id != 0){
            books = repository.findByTitleAndGenre(title, genre_id);
        } 
        else if (title != "" && author_id != null && genre_id == 0) {
            books = repository.findByAuthorsAndGenre(author_id, genre_id);
        }
        else {
            books = repository.findAll();
        }
        model.addAttribute("title", title);
        model.addAttribute("author_ids", author_id);
        model.addAttribute("genre_id", genre_id);
        model.addAttribute("count_books", books.size());

        List<Author> authors = authorRepository.findAll();
        List<Genre> genres = genreRepository.findAll();
        // Set<Author> authors = new HashSet();
        // Set<Genre> genres = new HashSet();
        // for (Book book : books) {
        //     authors.addAll(book.getAuthors());
        //     genres.addAll(book.getGenres());
        // }
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        model.addAttribute("books", books);
        return "book/search";
    }
    
    // ?????????????????? ?????????? ?????? ???????????????? ??????????
    @GetMapping("/new")
    @PreAuthorize("hasAuthority('manager')")
    public String newBook(Model model){
        List<BookType> bookTypeList = bookTypeRepository.findAll();
        List<Author> authorsList = authorRepository.findAll();
        List<Genre> genresList = genreRepository.findAll();
        List<Library> libraryList = libraryRepository.findAll();
        model.addAttribute("bookTypeList", bookTypeList);
        model.addAttribute("authorsList", authorsList);
        model.addAttribute("genresList", genresList);
        model.addAttribute("libraryList", libraryList);
        // model.addAttribute("book", new Book());
        return "book/new";
    }

    // ???????????????? ??????????
    @PostMapping()
    @PreAuthorize("hasAuthority('manager')")
    public String createBook( //@ModelAttribute("book") Book book,
        @RequestParam(value = "title", required = true) String title, 
        @RequestParam(value = "book_type_id", required = true) Long book_type_id, 
        @RequestParam(value = "author_id", required = true) List<Long> author_id,
        @RequestParam(value = "genre_id", required = true) List<Long> genre_id,
        @RequestParam(value = "book_type_id", required = true) Long library_id ) { 
        
        BookType bookType = bookTypeRepository.findById(book_type_id).get();
        Library library = libraryRepository.findById(library_id).get();

        Book book = repository.save(new Book(title, "", "", "", bookType, library));

        for (Long id : author_id) {
            Author author = authorRepository.findById(id).get();
            author.addBook(book);
            authorRepository.save(author);
        }

        for (Long id : genre_id) {
            Genre genre = genreRepository.findById(id).get();
            genre.addBook(book);
            genreRepository.save(genre);
        }
        return "redirect:/book";
    }





    // ?????????????????? ?????????? ??????????
    @GetMapping("/{vendor_code}") // {title}-
    public String oneBook(Model model, @PathVariable("vendor_code") Long vendor_code){ // @PathVariable("title") String title, 
        User user = getCurrentUser();
        model.addAttribute("user", user);

        Book book = repository.findByVendorCode(vendor_code);
        model.addAttribute("book", book);

        model.addAttribute("isAvailable", true);
        model.addAttribute("isBook_In_CurrentUser", false);
        model.addAttribute("is_CurrentUser_Standing_For_Book", false);
        model.addAttribute("isLeasedBook", false);


        List<UserBook> userBooks = userBookRepository.findListByBookId(vendor_code);

        Date today = Calendar.getInstance().getTime();
        Long countUsersStandingForBook = 0L;
        for (UserBook userBook : userBooks) {
                    System.out.println("Current Book's return date = " + userBook.getReturn_dateStr() + " and get time = " + String.valueOf(userBook.getReturn_date().getTime()));
            if (userBook.getReturn_date().getTime() != 0L
                && (userBook.getIssue_date().compareTo(today)) <= 0 && (userBook.getReturn_date().compareTo(today)) >= 1) { // ???????? ?????? ?????????????? ????????????
                model.addAttribute("isAvailable", false);
                model.addAttribute("isLeasedBook", true);
                model.addAttribute("currentUserBook", userBook);
                if (user.getId().equals(userBook.getUser().getId())) { // ???????? ?????????? ?? ???????????? ?? ???????????????? ????????????????????????
                    model.addAttribute("isBook_In_CurrentUser", true);
                    model.addAttribute("idUserBook_Where_CurrentUser_Lease_Book", userBook.getId());
                }

                if (user.isManager()) {
                    model.addAttribute("idUserBook_CurrentLease", userBook.getId());
                }
            }
            System.out.println("test1 " + String.valueOf(Long.valueOf(userBook.getReturn_date().getTime()) == 0L));
            System.out.println("test2 " + String.valueOf((userBook.getIssue_date().compareTo(today))));
            System.out.println("test3 " + String.valueOf(userBook.getIssue_date().getTime()));
            System.out.println("test4 " + String.valueOf(today.getTime()));
            // int result1 = userBook.getIssue_date().compareTo(today);
            if (userBook.getReturn_date().getTime() == 0L) { // ???????? ?????? ??????????  ( //  && (userBook.getIssue_date().compareTo(today)) >= 0 // ?? ?? ?????????????????????? ?????? ?? ???????????? ) 
                countUsersStandingForBook += 1;
                model.addAttribute("isAvailable", false);
                System.out.println("current userbook is reserve");
                if (user.getId().equals(userBook.getUser().getId())) { // ???????? ???????????????????????? ?????????????????? ?? ??????????????
                    System.out.println("current user v ocheredi");
                    model.addAttribute("is_CurrentUser_Standing_For_Book", true);
                    model.addAttribute("idUserBook_Where_CurrentUser_Standing_For_Book", userBook.getId());
                }
                
            }
        }
        model.addAttribute("countUsers_Standing_For_Book", countUsersStandingForBook);
        System.out.println("countUsersStandingForBook is more that 1 = " + String.valueOf(countUsersStandingForBook >= 1));
        if (user.isManager()) {
            System.out.println("isManager = true");
            List<UserBook> reserveList = userBookRepository.findReserveListByBookId(vendor_code);
            System.out.println(String.valueOf(reserveList.size()));
            if (reserveList.size() >= 1) {
                UserBook firstReserve = reserveList.get(0);
                model.addAttribute("idUserBook_FirstReserve", firstReserve.getId());
                System.out.println("idUserBook_FirstReserve = " + String.valueOf(firstReserve.getId()));
                model.addAttribute("currentUserBook", firstReserve);
            }
        }



        // UserBook currentUserBook = null;
        // UserBook lastUserBook = null;
        // Date today = Calendar.getInstance().getTime();
        // Date currentIssueDate = today;
        // Date lastReturnDate = today;

        // Long countUsersStandingForBook = 0L;
        // for (UserBook userBook : userBooks) {
        //     int result1 = userBook.getIssue_date().compareTo(currentIssueDate);
        //     int result2 = userBook.getReturn_date().compareTo(lastReturnDate);
        //     if (result1 < 0 && result2 >= 0){ // issue_date  is before  lastIssueDate   &&  return_date  is after or today  lastReturnDate
        //         lastReturnDate = userBook.getReturn_date();
        //         currentUserBook = userBook;
        //         lastUserBook = userBook;
        //     } else if (result1 >= 1 && result2 >= 0) { // issue_date  is after or today  lastIssueDate   &&  return_date  is after or today  lastReturnDate
        //         lastUserBook = userBook; // ???????? ?????? ???? ?????? ?? ??????????????
        //         countUsersStandingForBook++;
        //         if (user.getId().equals(userBook.getUser().getId())){
        //             model.addAttribute("isCurrentUserStandingForBook", true);
        //             model.addAttribute("idUserBookWhereCurrentUserStandingForBook", userBook.getId());
        //         }
        //     }
        // }

        // model.addAttribute("isAvailable", true);
        // model.addAttribute("isBookInCurrentUser", false);

        // if (currentUserBook != null){
        //     if (lastReturnDate.compareTo(today) >= 0){ // after or today
        //         model.addAttribute("isAvailable", false);
        //         model.addAttribute("currentUserBook", currentUserBook);
        //         model.addAttribute("lastUserBook", lastUserBook);
        //         // countUsersStandingForBook -= 1; // ???????????? ?? ???????? ?????????? ?? ???????????? ????????????
        //         model.addAttribute("countUsersStandingForBook", countUsersStandingForBook);
        //         if (user.getId().equals( currentUserBook.getUser().getId() )){
        //             model.addAttribute("isBookInCurrentUser", true);
        //             model.addAttribute("idUserBookWhereCurrentUserReserveBook", currentUserBook.getId());
        //             //.............................................................................................................. idUserBookWhereCurrentUserReserveBook
        //         }
        //     } else {
        //         model.addAttribute("isAvailable", true);
        //     }
        // }

        // Book book = repository.findByVendorCode(vendor_code);
        return "book/one";
    }
    
    // ???????????????? ??????????????????????
    @PostMapping("{vendor_code}/comments/new")
    public String createComment(@RequestParam(value = "content") String content, @PathVariable("vendor_code") Long vendor_code) { 
        if (content == null){
            return null;
        }
        User user = getCurrentUser();
        // if (user == null){
        //     return "redirect:/books/{vendor_code}";
        // }
        Book book = repository.findByVendorCode(vendor_code);
        Comment comment = commentRepository.save(new Comment(user, book, content));
        // // commentRepository.save(comment);
        user.addComment(comment);
        book.addComment(comment);
        userRepository.save(user);
        repository.save(book);
        return "redirect:/book/{vendor_code}";
    }


    // ???????????????????????? ?????????? ???? ????????
    @PostMapping("/{vendor_code}/reserve")
    public String reserveBook(@PathVariable("vendor_code") Long vendor_code){
        User user = getCurrentUser();
        Book book = repository.findByVendorCode(vendor_code);

        UserBook userBook = userBookRepository.save(new UserBook(user, book, Calendar.getInstance().getTime(), new Date(0))); 
        user.addUserBook(userBook);
        book.addUserBook(userBook);
        userRepository.save(user);
        repository.save(book);
        return "redirect:/book/{vendor_code}";
    }

    // ???????????? ?? ?????????????? ???? ?????????? 
    @PostMapping("/{vendor_code}/to_stand_in_line")
    public String toStandInLineForBook(@PathVariable("vendor_code") Long vendor_code) {
        User user = getCurrentUser();
        Book book = repository.findByVendorCode(vendor_code);

        Calendar nextDayAfterLastReserve = Calendar.getInstance();

        List<UserBook> reserveList = userBookRepository.findReserveListByBookId(vendor_code);
        if (reserveList.size() >= 1){
            UserBook lastReserve = reserveList.get(reserveList.size() - 1);
            nextDayAfterLastReserve.setTime(lastReserve.getIssue_date());
        }
        nextDayAfterLastReserve.add(Calendar.DAY_OF_MONTH, 1);

        UserBook reserveUB = userBookRepository.save(new UserBook(user, book, nextDayAfterLastReserve.getTime(), new Date(0)));

        user.addUserBook(reserveUB);
        book.addUserBook(reserveUB);
        repository.save(book);
        userRepository.save(user);
        return "redirect:/book/{vendor_code}";
    }

    // ?????????? ???? ?????????????? ???? ??????????
    @PostMapping("/{vendor_code}/to_stop_stand_in_line")
    public String toStopStandInLineForBook(@PathVariable("vendor_code") Long vendor_code, @RequestParam(value = "user_book_id") Long user_book_id) {
        userBookRepository.deleteById(user_book_id);
        return "redirect:/book/{vendor_code}";
    }

    // ?????????????????? ????????????
    @PostMapping("/{vendor_code}/extend_lease")
    public String toExtendReserveBook(@PathVariable("vendor_code") Long vendor_code, @RequestParam(value = "user_book_id") Long user_book_id) {
        User user = getCurrentUser();
        Book book = repository.findByVendorCode(vendor_code);
        UserBook currentUserBook = userBookRepository.findById(user_book_id).get();

        // Set<UserBook> userBooks = book.getUserBooks();

        // for (UserBook userBook : userBooks) {
        //     int result1 = userBook.getIssue_date().compareTo(currentUserBook.getReturn_date());
        //     if (result1 >= 1){ // issue_date  is after  currentUserBook.return_Date - ?????? ?? ??????????????
        //         System.out.print("LOLOLOL " + userBook.getIssue_dateStr() + " " + currentUserBook.getReturn_dateStr());
        //         userBookRepository.deleteById(userBook.getId()); // ???? ?????????????? ????????????-????
        //     }
        // }

        Calendar monthLater = Calendar.getInstance();
        monthLater.add(Calendar.MONTH, 1);
        currentUserBook.setReturn_date(monthLater.getTime());
        userBookRepository.save(currentUserBook);

        return "redirect:/book/{vendor_code}";
    }

    // ???????????? ???????????? ?????????? ????????????????????
    @PostMapping("/{vendor_code}/lease_out")
    public String leaseOut(@PathVariable("vendor_code") Long vendor_code, @RequestParam(value = "user_book_id") Long  user_book_id) {
        System.out.println("user_book_id = " + String.valueOf(user_book_id));
        UserBook currentUserBook = userBookRepository.findById(user_book_id).get();
        Calendar monthLater = Calendar.getInstance();
        monthLater.add(Calendar.MONTH, 1);
        
        currentUserBook.setIssue_date(Calendar.getInstance().getTime()); // today
        currentUserBook.setReturn_date(monthLater.getTime());
        userBookRepository.save(currentUserBook);
        return "redirect:/book/{vendor_code}";
    }

    // ???????????? ???????????? ????????????????????
    @PostMapping("/{vendor_code}/stop_lease")
    public String stopLease(@PathVariable("vendor_code") Long vendor_code, @RequestParam(value = "user_book_id") Long  user_book_id) {
        UserBook currentUserBook = userBookRepository.findById(user_book_id).get();
        currentUserBook.setReturn_date(Calendar.getInstance().getTime()); // today
        userBookRepository.save(currentUserBook);
        return "redirect:/book/{vendor_code}";
    }
    

    // ?????????????????? ?????????? ?????? ???????????????????????????? ??????????
    @GetMapping("/{vendor_code}/edit")
    public String editBook(Model model, @PathVariable("vendor_code") Long vendor_code){
        List<BookType> bookTypeListAll = bookTypeRepository.findAll();
        List<Author> authorsListAll = authorRepository.findAll();
        List<Genre> genresListAll = genreRepository.findAll();
        List<Library> libraryListAll = libraryRepository.findAll();

        Book book = repository.findByVendorCode(vendor_code);
        model.addAttribute("book", book);
        // List<Author> authorsList = List.copyOf(book.getAuthors());
        // List<Genre> genresList = List.copyOf(book.getGenres());
        List<Author> authorsList = authorRepository.findByBookVendorCode(vendor_code);
        List<Genre> genresList = genreRepository.findByBookVendorCode(vendor_code);
        model.addAttribute("title", book.getTitle());
        model.addAttribute("book_type_id", book.getBookType().getId());
        model.addAttribute("authorsList", authorsList);
        model.addAttribute("genresList", genresList);
        model.addAttribute("library_id", book.getLibrary().getId());

        model.addAttribute("bookTypeListAll", bookTypeListAll);
        model.addAttribute("authorsListAll", authorsListAll);
        model.addAttribute("genresListAll", genresListAll);
        model.addAttribute("libraryListAll", libraryListAll);
        return "book/edit";
    }

    // ???????????????????? ??????????
    @PatchMapping("/{vendor_code}")
    public String updateBook(//@ModelAttribute("book") Book book, 
        @PathVariable("vendor_code") Long vendor_code,
        @RequestParam(value = "title", required = true) String title, 
        @RequestParam(value = "book_type_id", required = true) Long book_type_id, 
        @RequestParam(value = "author_id", required = true) List<Long> author_id,
        @RequestParam(value = "genre_id", required = true) List<Long> genre_id,
        @RequestParam(value = "book_type_id", required = true) Long library_id ) { 
            
        BookType bookType = bookTypeRepository.findById(book_type_id).get();
        Library library = libraryRepository.findById(library_id).get();

        Book book = repository.findByVendorCode(vendor_code);
        book.setTitle(title);
        book.setBookType(bookType);
        book.setLibrary(library);
        repository.save(book);
        
        for (Long id : author_id) {
            Author author = authorRepository.findById(id).get();
            // System.out.println(author.toString());
            author.addBook(book);
            authorRepository.save(author);
        }
        for (Long id : genre_id) {
            Genre genre = genreRepository.findById(id).get();
            genre.addBook(book);
            genreRepository.save(genre);
        }

        List<Author> authorsList = authorRepository.findByBookVendorCode(vendor_code);
        List<Genre> genresList = genreRepository.findByBookVendorCode(vendor_code);
        for (Author author : authorsList) {
            if (!author_id.contains(author.getId())) {
                // System.out.println(author.toString());
                author.removeBook(book);
                authorRepository.save(author);
            }
        }
        for (Genre genre : genresList) {
            if (!genre_id.contains(genre.getId())) {
                genre.removeBook(book);
                genreRepository.save(genre);
            }
        }

        return "redirect:/book/{vendor_code}";
    }

    // ???????????????? ??????????
    @DeleteMapping("/{vendor_code}")
    public String deleteBook(@PathVariable("vendor_code") Long vendor_code){
        // repository.deleteByVendorCode(vendor_code);
        return "redirect:/book";
    }
    
    
}
