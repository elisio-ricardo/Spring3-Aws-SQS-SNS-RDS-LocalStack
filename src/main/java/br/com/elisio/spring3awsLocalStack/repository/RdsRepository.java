package br.com.elisio.spring3awsLocalStack.repository;


import br.com.elisio.spring3awsLocalStack.entity.Aluno;
import org.hibernate.dialect.MySQLStorageEngine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


//public interface RdsRepository extends CrudRepository<Aluno, Integer>
@Repository
public interface RdsRepository extends JpaRepository<Aluno, Integer> {

    List<Aluno> findAlunoByNomeContainingIgnoreCase(String nome);

}