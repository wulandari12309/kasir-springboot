package com.kasir_baru.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class KasirConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(request->request
                        .requestMatchers("/daftar-kasir", "/login-kasir", "/daftar-page", "/login-page", "/dashboard-kasir", "/dashboard-page", "/transaksi-page",
                                "/stok-page", "/laporan-page", "/pengaturan-page", "/tambah-barang", "/hapus-barang/**", "/**", "/update/**", "/next-kode", "/tambah",
                                "/transaksi", "/transaksi-nama-kasir", "/search-barang", "/simpan-transaksi", "/hitungKembalian", "/proses-transaksi", "/laporan",
                                "/laporan/detail", "/laporan/keuntungan-bulanan", "/laporan/keuntungan-harian", "/keuntungan-bulanan/download/**",
                                "/keuntungan-harian/download/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form->form
                        .loginPage("/login-page")
                        .loginProcessingUrl("/login-page")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login-page")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .csrf(csrf->csrf.disable());
        return httpSecurity.build();
    }
}
