package com.adcorreajr.vendas.service.impl;

import com.adcorreajr.vendas.domain.entity.Usuario;
import com.adcorreajr.vendas.domain.repository.UsuarioRepository;
import com.adcorreajr.vendas.exception.SenhaInvalidaException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UserDetailsService {


    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Transactional
    public Usuario salvar(Usuario usuario){
        return usuarioRepository.save(usuario);
    }


    public UserDetails autenticar (Usuario usuario){
        UserDetails user = loadUserByUsername(usuario.getLogin());
        boolean senhasBatem = encoder.matches(usuario.getSenha(), user.getPassword());

        if(senhasBatem){
            return  user;
        }

        throw new SenhaInvalidaException();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Usuario usuario = usuarioRepository.findByLogin(username)
               .orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado."));

       String[] roles = usuario.isAdmin()
               ? new String[] {"USER", "ADMIN"} : new String[] {"USER"};

       return User
               .builder()
               .username(usuario.getLogin())
               .password(usuario.getSenha())
               .roles(roles)
               .build();

       /* if(!username.equals("adcorrea")){
            throw  new UsernameNotFoundException("Usuário não enoontrado na base.");
        }

        return User.builder()
                .username("adcorrea")
                .password(encoder.encode("1234"))
                .roles("USER","ADMIN")
                .build();*/
    }
}
