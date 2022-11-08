package com.aminov.corporativelibrary;

import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

@Configuration
public class LoadDataBase {

    private static final Logger log = LoggerFactory.getLogger(LoadDataBase.class);

    @Bean
    CommandLineRunner initDataBase(
        AuthorRepository authorRepository,
        BookRepository bookRepository,
        BookTypeRepository bookTypeRepository,
        CommentRepository commentRepository,
        GenreRepository genreRepository,
        LibraryRepository libraryRepository,
        UserRepository userRepository,
        UserBookRepository userBookRepository) {
        return args -> {
            // создание сущностей, которые пока не зависят друг от друга
            BookType bt1 = bookTypeRepository.save(new BookType("Бумажная"));
            BookType bt2 = bookTypeRepository.save(new BookType("Электронная"));
            BookType bt3 = bookTypeRepository.save(new BookType("Аудио"));

            Genre g1 = genreRepository.save(new Genre("Поэма"));
            Genre g2 = genreRepository.save(new Genre("Рассказ"));
            Genre g3 = genreRepository.save(new Genre("Роман"));

            Author a1 = authorRepository.save(new Author("Pushkin", "Alexander", "Sergeyevich"));
            Author a2 = authorRepository.save(new Author("Tolstoy", "Lev", "Nikolayevich"));
            Author a3 = authorRepository.save(new Author("Turgenev", "Ivan", "Sergeyevich"));
            Author a4 = authorRepository.save(new Author("Bradbury", "Ray Douglas", null));
            
            Library l1 = libraryRepository.save(new Library("Детская", "г. Уфа, ул. Пушкина, д. 1"));
            Library l2 = libraryRepository.save(new Library("Школьная", "г. Уфа, ул. Пушкина, д. 2"));
            Library l3 = libraryRepository.save(new Library("Университетская", "г. Уфа, ул. Пушкина, д. 3"));

            User u1 = userRepository.save(new User("Aminov", "Arslan", "Gaynetdinovich", 1L));
            User u2 = userRepository.save(new User("Jackson", "Michael", "Joseph", 0L));

            // создание книг, пока без связей с авторам, жанрами и др., но сразу с сохранением в БД, чтобы был id, 
            // т.к без него при каждом их обновлении в БД создается новая строка, а не обновляется старая
            Book b1 = bookRepository.save(new Book("K-001", "Капитанская дочка", "крутое описание", "-", "-", bt3, l2));
            Book b2 = bookRepository.save(new Book("M-001", "Муму", "крутое описание2", "-", "-", bt1, l1));
            Book b3 = bookRepository.save(new Book("4-001", "451 градус по Фаренгейту", "крутое описание3", "-", "-", bt1, l3));
            Book b4 = bookRepository.save(new Book("W-001", "Война и мир", "крутое описание4", "-", "-", bt1, l2));
            Book b5 = bookRepository.save(new Book("W-001", "Война и мир", "крутое описание5", "-", "-", bt1, l3));
            Book b6 = bookRepository.save(new Book("W-001", "Война и мир", "крутое описание5", "-", "-", bt2, l3));
            Book b7 = bookRepository.save(new Book("4-001", "451 градус по Фаренгейту", "крутое описание3", "-", "-", bt2, l3));

            // добавление книг к авторам
            a1.addBook(b1);
            a3.addBook(b2);
            a4.addBook(b3);
            a2.addBook(b4);
            a2.addBook(b5);
            a2.addBook(b6);
            a4.addBook(b7);
            
            // добавление книг к жанрам
            g3.addBook(b1);
            g2.addBook(b2);
            g3.addBook(b3);
            g3.addBook(b4);
            g3.addBook(b5);
            g3.addBook(b6);
            g3.addBook(b7);

            // обновление авторов в БД вместе с книгами, которые тоже заносятся в БД; и еще обновляется промежуточная таблица автор-книга
            authorRepository.save(a1);
            authorRepository.save(a2);
            authorRepository.save(a3);
            authorRepository.save(a4);

            // обновление жанров в БД всместе с книгами; и еще обновляется промежуточная таблица жанр-книга
            genreRepository.save(g1);
            genreRepository.save(g2);
            genreRepository.save(g3);



            // добавление "Книг у пользователей"
            UserBook ub1 = userBookRepository.save(new UserBook(u1, b2, 
                new GregorianCalendar(2022, 10, 5).getTime(), 
                new GregorianCalendar(2022, 11, 5).getTime()));
            UserBook ub2 = userBookRepository.save(new UserBook(u1, b3, 
                new GregorianCalendar(2022, 10, 15).getTime(), 
                new GregorianCalendar(2022, 11, 15).getTime()));
            UserBook ub3 = userBookRepository.save(new UserBook(u2, b4, 
                new GregorianCalendar(2022, 10, 2).getTime(), 
                new GregorianCalendar(2022, 11, 2).getTime()));
            UserBook ub4 = userBookRepository.save(new UserBook(u2, b4, 
                new GregorianCalendar(2022, 10, 2).getTime(), 
                new GregorianCalendar(2022, 11, 2).getTime()));
            UserBook ub5 = userBookRepository.save(new UserBook(u2, b7, 
                new GregorianCalendar(2022, 10, 4).getTime(), 
                new GregorianCalendar(2022, 11, 4).getTime()));
                
            u1.addUserBook(ub1);
            u1.addUserBook(ub2);
            u2.addUserBook(ub3);
            u2.addUserBook(ub4);
            u2.addUserBook(ub5);

            b2.addUserBook(ub1);
            b3.addUserBook(ub2);
            b4.addUserBook(ub3);
            b4.addUserBook(ub4);
            b7.addUserBook(ub5);



            // добавление коментариев
            Comment c1 = commentRepository.save(new Comment(u1, b2, "грустно"));
            Comment c2 = commentRepository.save(new Comment(u1, b3, "огонь!"));
            Comment c3 = commentRepository.save(new Comment(u1, b4, "длинное"));
            Comment c4 = commentRepository.save(new Comment(u2, b1, "круто"));

            u1.addComment(c1);
            u1.addComment(c2);
            u1.addComment(c3);
            u2.addComment(c4);

            b2.addComment(c1);
            b3.addComment(c2);
            b4.addComment(c3);
            b1.addComment(c4);

            // обновление книг вместе с "книгами у пользователя" и комментариями, которые тоже заносятся в БД
            bookRepository.save(b1);
            bookRepository.save(b2);
            bookRepository.save(b3);
            bookRepository.save(b4);
            bookRepository.save(b7);

            // обновление пользователей вместе с "книгами у пользователя" и комментариями
            userRepository.save(u1);
            userRepository.save(u2);

            //log.info("count of books - " + String.valueOf(bookRepository.findAll().size()));
            log.info("-------------- entities saved in repositories --------------");bookRepository.findAll().size();
        };
    }
    
}
