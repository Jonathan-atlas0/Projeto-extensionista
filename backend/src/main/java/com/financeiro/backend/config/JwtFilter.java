package com.financeiro.backend.config;

import com.financeiro.backend.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
            ) throws ServletException, IOException {

        //Pega o cabeçalho Autorization da requisição
        final  String authheader = request.getHeader("Authorization");

        //Caso n tenha token ou n começar com "Bearer ", ele ignora
        if(authheader == null || !authheader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        //Remove o inicio "Brear " e fica apenas com o token
        final String token = authheader.substring(7);

        //Extrai o email de dentro do token
        final String email = jwtUtil.extrairEmail(token);

        // Caso tenha email e o usuario ainda n foi autenticado
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null){

            //Busca o usuario no banco pelo email
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // Valida se o token é verdadeiro e se n expirou
            if (jwtUtil.tokenValido(token, userDetails)){

                // Cria objeto de autenticação
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                //Add detalhes da requisição
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //Registra o usuario como autenticado no spring
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        //Passa para o proximo filtro ou controller
        filterChain.doFilter(request, response);
    }
}
