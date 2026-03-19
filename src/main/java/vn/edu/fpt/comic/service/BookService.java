package vn.edu.fpt.comic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.fpt.comic.entity.Book;
import vn.edu.fpt.comic.repository.BookRepository;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

//    @Autowired
//    private AuthorService authorService;
//
//    @Autowired
//    private CategoryService categoryService;
//
//    @Autowired
//    private LanguageService languageService;
//
//    @Autowired
//    private PublisherService publisherService;
//
//    @Autowired
//    private SeriesService seriesService;
//
//    @Autowired
//    private TranslatorService translatorService;
//
//    @Autowired
//    private BookTranslatorRepository bookTranslatorRepository;

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(Integer id) {
        return bookRepository.findById(id).orElse(null);
    }
}
