package br.com.pedroxsqueiroz.camsresourceserver.daos;

import org.springframework.stereotype.Repository;

import br.com.pedroxsqueiroz.camsresourceserver.models.ServerModel;

@Repository
public interface ServerDao extends NodeDao<ServerModel> {

}
