package br.com.bossini.cadastrousurionicofatecipinoitefirebase;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class EditarUsuarioActivity extends AppCompatActivity {

    private EditText nomeEditText, foneEditText, emailEditText;
    private ImageView fotoParaEdicaoImageView;
    private FirebaseDatabase database;
    private DatabaseReference referenceUsuario;
    private StorageReference storageRootReference;
    private static final int REQUISICAO_TIRAR_FOTO = 547;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabConfirmarEdicao);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = nomeEditText.getEditableText().toString();
                String fone = foneEditText.getEditableText().toString();
                String email = emailEditText.getEditableText().toString();
                Usuario.getInstance().setNome(nome);
                Usuario.getInstance().setFone(fone);
                Usuario.getInstance().setEmail(email);
                referenceUsuario.setValue(Usuario.getInstance());
                finish();
            }
        });
        nomeEditText = (EditText) findViewById(R.id.nomeEditText);
        foneEditText = (EditText) findViewById(R.id.foneEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        fotoParaEdicaoImageView = (ImageView) findViewById(R.id.fotoParaEdicaoImageView);

        database = FirebaseDatabase.getInstance();
        referenceUsuario = database.getReference("usuario");
        storageRootReference = FirebaseStorage.getInstance().getReference();
        configurarComponentesVisuais();
    }
    private void configurarComponentesVisuais (){
        nomeEditText.setText(Usuario.getInstance().getNome() != null ? Usuario.getInstance().getNome() : "");
        foneEditText.setText(Usuario.getInstance().getFone() != null ? Usuario.getInstance().getFone() : "");
        emailEditText.setText(Usuario.getInstance().getEmail() != null ? Usuario.getInstance().getEmail() : "");
        if (Usuario.getInstance().getFoto() != null){
            fotoParaEdicaoImageView.setImageBitmap(Usuario.getInstance().getFoto());
        }
    }

    public void tirarFoto (View view){
        Intent i = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, REQUISICAO_TIRAR_FOTO);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (requestCode == REQUISICAO_TIRAR_FOTO){
           if (resultCode == Activity.RESULT_OK){
               Bitmap foto = (Bitmap) data.getExtras().get("data");
               Usuario.getInstance().setFoto(foto);
               fotoParaEdicaoImageView.setImageBitmap(foto);
               StorageReference fotoReference = storageRootReference.child("img/foto.png");
               ByteArrayOutputStream baos = new ByteArrayOutputStream();
               foto.compress(Bitmap.CompressFormat.PNG, 0, baos);
               byte [] fotoComprimida = baos.toByteArray();
               fotoReference.putBytes(fotoComprimida).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                   @Override
                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       Toast.makeText(EditarUsuarioActivity.this, getString(R.string.ok_subiu), Toast.LENGTH_SHORT).show();
                   }
               });
           }
       }
    }
}
