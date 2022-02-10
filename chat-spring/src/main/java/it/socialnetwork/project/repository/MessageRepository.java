package it.socialnetwork.project.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import it.socialnetwork.project.model.UserMessage;

@Repository
public interface MessageRepository extends JpaRepository<UserMessage, Long> {
	
		//TODO limitarli a 20
		//ottieni i messaggi della chat 
		@Query("SELECT t FROM UserMessage t where t.chatId = :id") 
		List<UserMessage> findLastMessages(@Param("id") Long id);
		
		// se l'utente scarica i vecchi messaggi li ha letti
		@Modifying
		@Transactional
		@Query("UPDATE UserMessage t SET t.hasReceived = true where t.chatId = :id and t.recipient = :user") 
		void setAllHasReceivedToTrue(@Param("id") Long id, @Param("user") String user);
	
		
		@Modifying
		@Transactional
		@Query("UPDATE UserMessage t SET t.hasReceived = true where t.uuid = :id") 
		void setHasReceivedToTrue(@Param("id") UUID id);
		
		
		
		
		@Query("SELECT t FROM UserMessage t WHERE t.uuid = :id") 
		UserMessage findMessageByMessageId(@Param("id") UUID id);
		
		
		@Query("SELECT t.chatId FROM UserMessage t where t.uuid = :uuid") 
		Long test(@Param("uuid") UUID uuid);
		
		
		
		
}
