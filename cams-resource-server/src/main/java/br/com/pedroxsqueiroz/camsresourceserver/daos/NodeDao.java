package br.com.pedroxsqueiroz.camsresourceserver.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.pedroxsqueiroz.camsresourceserver.models.NodeModel;

public interface NodeDao<T extends NodeModel> extends JpaRepository<T, Integer>, JpaSpecificationExecutor<T>{

}
