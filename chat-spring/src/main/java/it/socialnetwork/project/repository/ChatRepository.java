package it.socialnetwork.project.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.socialnetwork.project.model.Chat;


@Repository
public interface ChatRepository extends JpaRepository<Chat, Long>{

	
	@Query("SELECT t.id FROM Chat t where (t.user1 = :user1 and t.user2 = :user2) or (t.user1 = :user2 and t.user2 = :user1)") 
	Long findChatId(@Param("user1") String user1, @Param("user2") String user2);
	
	
	
	
	
}
