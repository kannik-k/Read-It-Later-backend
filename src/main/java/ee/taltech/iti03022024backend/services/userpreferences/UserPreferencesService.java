package ee.taltech.iti03022024backend.services.userpreferences;

import ee.taltech.iti03022024backend.dto.book.BookDtoOut;
import ee.taltech.iti03022024backend.dto.userpreferences.UserPreferencesDtoIn;
import ee.taltech.iti03022024backend.dto.userpreferences.UserPreferencesDtoOut;
import ee.taltech.iti03022024backend.entities.book.BookEntity;
import ee.taltech.iti03022024backend.entities.usepreferences.UserPreferencesEntity;
import ee.taltech.iti03022024backend.entities.wishlist.WishListEntity;
import ee.taltech.iti03022024backend.exceptions.NameAlreadyExistsException;
import ee.taltech.iti03022024backend.exceptions.NotFoundException;
import ee.taltech.iti03022024backend.mappers.book.BookMapper;
import ee.taltech.iti03022024backend.mappers.userpreferences.UserPreferencesMapper;
import ee.taltech.iti03022024backend.repositories.books.BookRepository;
import ee.taltech.iti03022024backend.repositories.userpreferences.UserPreferencesRepository;
import ee.taltech.iti03022024backend.response.book.BookPageResponse;
import ee.taltech.iti03022024backend.services.genre.GenreService;
import ee.taltech.iti03022024backend.specifications.userpreferences.UserPreferencesSpecifications;
import ee.taltech.iti03022024backend.specifications.wishlist.WishListSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserPreferencesService {

    private final UserPreferencesRepository userPreferencesRepository;
    private final UserPreferencesMapper userPreferencesMapper;
    private final GenreService genreService;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public UserPreferencesDtoOut addGenre(UserPreferencesDtoIn userPreferencesDtoIn, Long userId) {
        if (userPreferencesRepository.existsByUserIdAndGenreId(userId, userPreferencesDtoIn.getGenreId())) {
            throw new NameAlreadyExistsException("Genre already exists.");
        }

        UserPreferencesEntity userPreferencesEntity = userPreferencesMapper.toEntity(userPreferencesDtoIn);
        userPreferencesEntity.setUserId(userId);
        userPreferencesRepository.save(userPreferencesEntity);
        return userPreferencesMapper.toDto(userPreferencesEntity);
    }

    public List<UserPreferencesDtoOut> getGenres(Long userId) {
        Specification<UserPreferencesEntity> spec = Specification.where(UserPreferencesSpecifications.getByUserId(userId));
        List<UserPreferencesEntity> userPreferences = userPreferencesRepository.findAll(spec);
        return userPreferences.stream().map(entity -> {
            UserPreferencesDtoOut userPreferencesDtoOut = userPreferencesMapper.toDto(entity);
            Long genreId = entity.getGenreId();
            userPreferencesDtoOut.setGenreId(genreId);
            String genre = genreService.getGenreById(genreId);
            userPreferencesDtoOut.setGenre(genre);
            return userPreferencesDtoOut;
        }).toList();
    }

    public BookPageResponse getRecommendedBooks(Long userId, int page, int size) {
        Specification<UserPreferencesEntity> spec = Specification.where(UserPreferencesSpecifications.getByUserId(userId));

        Pageable pageable = PageRequest.of(page, size);

        List<UserPreferencesEntity> userPreferences = userPreferencesRepository.findAll(spec);

        List<Long> genresById = userPreferences.stream().map(UserPreferencesEntity::getGenreId).toList();
        Slice<BookEntity> books = bookRepository.findAllByGenreIdIn(genresById, pageable);

        List<BookDtoOut> booksList = books.stream().map(
                        bookEntity -> {
                            BookDtoOut bookDtoOut = bookMapper.toDto(bookEntity);
                            String bookGenre = genreService.getGenreById(bookEntity.getGenreId());
                            bookDtoOut.setGenre(bookGenre);
                            return bookDtoOut;
                        })
                .toList();
        boolean isLastPage = books.isLast();
        return new BookPageResponse(booksList, !isLastPage);
    }

    public void deleteGenre(Long userId, String genre) throws NotFoundException {
        Long genreId = genreService.getGenreByName(genre);
        if (genreId == null) {
            throw new NotFoundException("Genre not found: " + genre);
        }

        List<UserPreferencesEntity> userPreferences = userPreferencesRepository.findByUserIdAndGenreId(userId, genreId);

        if (userPreferences.isEmpty()) {
            throw new NotFoundException("No preferences found for user " + userId + " with genre " + genre);
        }

        userPreferencesRepository.deleteAll(userPreferences);
    }

    public void deleteByUserId(Long userId) {
        userPreferencesRepository.deleteByUserId(userId);
    }
}
