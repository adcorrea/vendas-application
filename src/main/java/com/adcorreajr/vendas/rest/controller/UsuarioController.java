package com.adcorreajr.vendas.rest.controller;


import com.adcorreajr.vendas.domain.entity.Usuario;
import com.adcorreajr.vendas.exception.SenhaInvalidaException;
import com.adcorreajr.vendas.rest.dto.CredentialsDTO;
import com.adcorreajr.vendas.rest.dto.TokenDTO;
import com.adcorreajr.vendas.security.jwt.JwtService;
import com.adcorreajr.vendas.service.impl.UsuarioServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioServiceImpl usuarioService;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario salvar(@RequestBody @Valid Usuario usuario){
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        return usuarioService.salvar(usuario);
    }


    @PostMapping("/auth")
    public TokenDTO autenticar(@RequestBody CredentialsDTO credenciais){
        try {
            {
                Usuario usuario = Usuario.builder()
                        .login(credenciais.getLogin())
                        .senha(credenciais.getSenha())
                        .build();

                UserDetails user = usuarioService.autenticar(usuario);

                String token = jwtService.gerarToken(usuario);
                return new TokenDTO(usuario.getLogin(), token);
            }
        }catch (UsernameNotFoundException ex){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }catch (SenhaInvalidaException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

}
