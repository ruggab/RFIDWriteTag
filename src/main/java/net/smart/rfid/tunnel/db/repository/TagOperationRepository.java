package net.smart.rfid.tunnel.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.smart.rfid.tunnel.db.entity.TagOperation;

@Repository
public interface TagOperationRepository extends JpaRepository<TagOperation, Long> {

	List<TagOperation> findByEpcOld(String epcOld);

	List<TagOperation> findByEpcNew(String epcNew);

}
