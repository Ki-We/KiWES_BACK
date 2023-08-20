package server.api.kiwes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import server.api.kiwes.domain.category.entity.Category;
import server.api.kiwes.domain.category.repository.CategoryRepository;
import server.api.kiwes.domain.category.type.CategoryType;
import server.api.kiwes.domain.club.entity.Club;
import server.api.kiwes.domain.club_category.entity.ClubCategory;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class KiwesApplicationTests {

	@Autowired
	private CategoryRepository categoryRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void sortByCategory() {
//		List<String> categories = new ArrayList<>();
//		categories.add("KPOP");
//		List<Club> club = new ArrayList<>();
//
//		for (String categoryString : categories) {
//			CategoryType type = CategoryType.valueOf(categoryString);
//			Category category = categoryRepository.findByName(type);
//			List<ClubCategory> clubCategories = category.getClubCategories();
//			clubCategories.forEach(clubCategory -> club.add(clubCategory.getClub()));
//		}
//		System.out.println(club);

	}

}
