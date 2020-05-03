package br.com.pedroxsqueiroz.camsresourceserver.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.pedroxsqueiroz.camsresourceserver.models.CamModel;

@Repository
public interface CamDao extends JpaRepository<CamModel, Integer>, JpaSpecificationExecutor<CamModel> {

}
