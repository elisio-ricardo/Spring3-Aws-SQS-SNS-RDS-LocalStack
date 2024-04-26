package br.com.elisio.spring3awsLocalStack.service;


import br.com.elisio.spring3awsLocalStack.DTO.AlunoRequest;
import br.com.elisio.spring3awsLocalStack.DTO.AlunoResponse;
import br.com.elisio.spring3awsLocalStack.Exception.AlunoException;
import br.com.elisio.spring3awsLocalStack.entity.Aluno;
import br.com.elisio.spring3awsLocalStack.repository.RdsRepository;
import br.com.elisio.spring3awsLocalStack.util.AlunoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RdsService {

    private static final String ALUNO_NAO_ENCONTRADO = "Aluno não encontrado";

    @Autowired
    private RdsRepository rdsRepository;

    public AlunoResponse criarAluno(AlunoRequest alunoRequest) {
        Aluno alunoNovo = AlunoConverter.toAluno(alunoRequest);
        return AlunoConverter.toAlunoResponse(rdsRepository.save(alunoNovo));
    }

    public AlunoResponse atualizarAluno(AlunoRequest alunoRequest) {
        if (rdsRepository.findById(alunoRequest.getIdAluno()).isEmpty())
            throw new AlunoException(ALUNO_NAO_ENCONTRADO);

        Aluno alunoNovo = AlunoConverter.toAluno(alunoRequest);

        return AlunoConverter.toAlunoResponse(rdsRepository.save(alunoNovo));
    }

    public List<AlunoResponse> buscarAluno(String nome) {
        return rdsRepository.findAlunoByNomeContainingIgnoreCase(nome)
                .stream().map(c -> AlunoConverter.toAlunoResponse((rdsRepository.save(c)))).collect(Collectors.toList());
    }


    public AlunoResponse excluirAluno(Integer idAluno) {
        if (rdsRepository.findById(idAluno).isEmpty())
            throw new AlunoException(ALUNO_NAO_ENCONTRADO);

        AlunoResponse alunoResponse = new AlunoResponse();
        alunoResponse.setIdAluno(idAluno);

        rdsRepository.deleteById(idAluno);
        return alunoResponse;
    }


}
