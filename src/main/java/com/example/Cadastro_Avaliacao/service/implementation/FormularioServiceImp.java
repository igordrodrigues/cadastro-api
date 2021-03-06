package com.example.Cadastro_Avaliacao.service.implementation;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Cadastro_Avaliacao.exception.ErroAutenticacao;
import com.example.Cadastro_Avaliacao.exception.RegraNegocioException;
import com.example.Cadastro_Avaliacao.model.entity.Formulario;
import com.example.Cadastro_Avaliacao.model.repository.FormularioRepository;
import com.example.Cadastro_Avaliacao.service.FormularioService;

@Service
public class FormularioServiceImp implements FormularioService{

	private FormularioRepository repository;

	@Autowired
	public FormularioServiceImp(FormularioRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional
	public Formulario salvar(Formulario formulario) {		
			validar(formulario);
			validarCpf(formulario.getCpf());		
			return repository.save(formulario);
	}

	@Override
	public void validarCpf(String cpf) {
		boolean exists = repository.existsByCpf(cpf);
		if(exists) {
			throw new RegraNegocioException("CPF já cadastrado");
		}
		
	}

	@Override
	@Transactional
	public Formulario atualizar(Formulario formulario) {
		Objects.requireNonNull(formulario.getCpf());
		return repository.save(formulario);
	}

	@Override
	@Transactional
	public void deletar(Formulario formulario) {
		Objects.requireNonNull(formulario);
		repository.delete(formulario);
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<Formulario> buscar(Formulario formularioFiltro) {
		Example example = Example.of(formularioFiltro, 
										ExampleMatcher
										.matching()
										.withIgnoreCase()
										.withStringMatcher(StringMatcher.CONTAINING) );
		
		return repository.findAll(example);
	}

	@Override
	public void validar(Formulario formulario) {
		if(formulario.getCpf() == null || formulario.getCpf().length() != 11) {
			throw new RegraNegocioException("CPF em branco ou inválido. Digite apenas os números");
		}
		if(formulario.getNome() == null || formulario.getNome().trim().equals("") ) {
			throw new RegraNegocioException("Campo Nome é obrigatório");
		}
		if(formulario.getUf()== null ) {
			throw new RegraNegocioException("Campo UF é obrigatório");
		}
		
	}

	@Override
	public Optional<Formulario> selecionaPorCpf(String cpf) {
		return repository.findByCpf(cpf);
	}

	@Override
	public Integer obterTotalPorEstado(String uf) {
		Integer result = repository.obterTotalPorEstado(uf);
		if(result == null) {
			result = 0;
		}
		return result;
	}

	
}
