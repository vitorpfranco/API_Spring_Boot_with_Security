package com.soulcode.Servicos.Services;

import com.soulcode.Servicos.Models.Cliente;
import com.soulcode.Servicos.Models.EnderecoCliente;
import com.soulcode.Servicos.Repositories.ClienteRepository;
import com.soulcode.Servicos.Repositories.EnderecoClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoClienteService {

    @Autowired
    EnderecoClienteRepository enderecoClienteRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Cacheable("enderecosCache")
    public List<EnderecoCliente> mostrarTodosEnderecosCliente(){
        return enderecoClienteRepository.findAll();
    }

    @Cacheable (value="enderecosCache", key="#idEnderecoCli")
    public EnderecoCliente mostrarUmEnderecoPeloId(Integer idEnderecoCli){
        Optional<EnderecoCliente> endereco = enderecoClienteRepository.findById(idEnderecoCli);
        return endereco.orElseThrow();
    }

    @CachePut(value="enderecosCache", key="#enderecoCliente.idEndereco")
    public EnderecoCliente cadastrarEnderecoDoCliente (EnderecoCliente enderecoCliente, Integer idCliente) throws Exception {
        Optional<Cliente> cliente = clienteRepository.findById(idCliente);
        if(cliente.isPresent()){
            enderecoCliente.setIdEndereco(idCliente);
            enderecoClienteRepository.save(enderecoCliente);

            cliente.get().setEnderecoCliente(enderecoCliente);
            clienteRepository.save(cliente.get());
            return enderecoCliente;
        }else{
            throw new Exception();
        }
    }
    @CachePut(value="enderecosCache", key="#enderecoCliente.idEndereco")
    public EnderecoCliente editarEndereco(EnderecoCliente enderecoCliente){
        return enderecoClienteRepository.save(enderecoCliente);
    }
}
