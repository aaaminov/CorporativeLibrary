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
            BookType paperBkTp = bookTypeRepository.save(new BookType("Бумажная"));
            BookType electronBkTp = bookTypeRepository.save(new BookType("Электронная"));
            BookType audioBkTp = bookTypeRepository.save(new BookType("Аудио"));

            Genre poem = genreRepository.save(new Genre("Поэма"));
            Genre rasskaz = genreRepository.save(new Genre("Рассказ"));
            Genre roman = genreRepository.save(new Genre("Роман"));

            Author pushkin = authorRepository.save(new Author("Pushkin", "Alexander", "Sergeyevich"));
            Author tolstoy = authorRepository.save(new Author("Tolstoy", "Lev", "Nikolayevich"));
            Author turgenev = authorRepository.save(new Author("Turgenev", "Ivan", "Sergeyevich"));
            Author bradbury = authorRepository.save(new Author("Bradbury", "Ray Douglas", null));
            
            Library libDetskaya = libraryRepository.save(new Library("Детская", "г. Уфа, ул. Пушкина, д. 1"));
            Library libShcool = libraryRepository.save(new Library("Школьная", "г. Уфа, ул. Пушкина, д. 2"));
            Library libUniver = libraryRepository.save(new Library("Университетская", "г. Уфа, ул. Пушкина, д. 3"));

            User arslanAminov = userRepository.save(new User("Aminov", "Arslan", "Gaynetdinovich", 1L));
            User michaelJackson = userRepository.save(new User("Jackson", "Michael", "Joseph", 0L));

            Book capDochka = new Book("K-001", "Капитанская дочка", "крутое описание", "-", "-", audioBkTp, libShcool);
            capDochka.addAuthor(pushkin); // добавление автора к книге
            capDochka.addGenre(roman); // добавление жанра к книге
            bookRepository.save(capDochka); // сохранение книги в БД
            authorRepository.save(pushkin); // надо обновить сущность автора, чтобы в промежуточной таблице AUTHOR_BOOK появилось соединение
            genreRepository.save(roman); // надо обновить сущность жанра, чтобы в промежуточной таблице GENRE_BOOK появилось соединение
            
            Book mymy = new Book("M-001", "Муму", "крутое описание2", "-", "-", paperBkTp, libDetskaya);
            mymy.addAuthor(turgenev);
            mymy.addGenre(rasskaz);
            bookRepository.save(mymy);
            authorRepository.save(turgenev);
            genreRepository.save(rasskaz);
            
            Book farengeit451 = new Book("4-001", "451 градус по Фаренгейту", "крутое описание3", "-", "-", paperBkTp, libUniver);
            farengeit451.addAuthor(bradbury);
            farengeit451.addGenre(roman);
            bookRepository.save(farengeit451);
            authorRepository.save(bradbury);
            //genreRepository.save(roman); // при первом повторении жанра в таблице GENRE_BOOK получается ошибка, проблема в id книги 
            
            Book warAndPeace = new Book("W-001", "Война и мир", "крутое описание4", "-", "-", paperBkTp, libShcool);
            warAndPeace.addAuthor(tolstoy);
            warAndPeace.addGenre(roman);
            bookRepository.save(warAndPeace);
            authorRepository.save(tolstoy);
            //genreRepository.save(roman);
            
            Book warAndPeace2 = new Book("W-001", "Война и мир", "крутое описание5", "-", "-", paperBkTp, libUniver);
            warAndPeace2.addAuthor(tolstoy);
            warAndPeace2.addGenre(roman);
            bookRepository.save(warAndPeace2);
            //authorRepository.save(tolstoy); // при первом повторении автора в таблице AUTHOR_BOOK получается ошибка
            //genreRepository.save(roman);
            
            Book warAndPeace3 = new Book("W-001", "Война и мир", "крутое описание5", "-", "-", electronBkTp, libUniver);
            warAndPeace3.addAuthor(tolstoy);
            warAndPeace3.addGenre(roman);
            bookRepository.save(warAndPeace3);
            //authorRepository.save(tolstoy);
            //genreRepository.save(roman);
            
            Book farengeit451_2 = new Book("4-001", "451 градус по Фаренгейту", "крутое описание3", "-", "-", electronBkTp, libUniver);
            farengeit451_2.addAuthor(bradbury);
            farengeit451_2.addGenre(roman);
            bookRepository.save(farengeit451_2);
            //authorRepository.save(bradbury);
            //genreRepository.save(roman);

            userBookRepository.save(new UserBook(arslanAminov, mymy, 
                new GregorianCalendar(2022, 10, 5).getTime(), 
                new GregorianCalendar(2022, 11, 5).getTime()));
            userBookRepository.save(new UserBook(arslanAminov, farengeit451, 
                new GregorianCalendar(2022, 10, 15).getTime(), 
                new GregorianCalendar(2022, 11, 15).getTime()));
            userBookRepository.save(new UserBook(michaelJackson, warAndPeace, 
                new GregorianCalendar(2022, 10, 2).getTime(), 
                new GregorianCalendar(2022, 11, 2).getTime()));
            userBookRepository.save(new UserBook(arslanAminov, capDochka, 
                new GregorianCalendar(2022, 10, 3).getTime(), 
                new GregorianCalendar(2022, 11, 3).getTime()));
            userBookRepository.save(new UserBook(michaelJackson, farengeit451_2, 
                new GregorianCalendar(2022, 10, 4).getTime(), 
                new GregorianCalendar(2022, 11, 4).getTime()));
            
            commentRepository.save(new Comment(arslanAminov, mymy, "грустно"));
            commentRepository.save(new Comment(arslanAminov, farengeit451, "огонь!"));
            commentRepository.save(new Comment(arslanAminov, warAndPeace, "длинное"));
            commentRepository.save(new Comment(michaelJackson, capDochka, "круто"));

            log.info("-------------- entities saved in repositories --------------");
        };
    }
    
}
