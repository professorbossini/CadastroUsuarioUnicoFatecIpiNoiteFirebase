package br.com.bossini.cadastrousurionicofatecipinoitefirebase;

import android.graphics.Bitmap;

import com.google.firebase.database.Exclude;

/**
 * Created by rodrigo on 9/29/17.
 */

public class Usuario {

    private static Usuario instance;
    private String nome, fone, email;
    private Bitmap foto;

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

    @Exclude
    public Bitmap getFoto() {
        return foto;
    }

    @Exclude
    public static Usuario getInstance (){
        return instance == null ? instance = new Usuario () : instance;
    }

    private Usuario (){

    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
