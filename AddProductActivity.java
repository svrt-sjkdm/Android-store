package com.example.svart_sjkdm.store;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.String;
import java.util.regex.Pattern;

public class AddProductActivity extends AppCompatActivity {

    EditText eTcodBarras, eTprecio, eTNombre, eTMarca, eTExist;
    EditText eTProv, eTDesc, eTNomProv, eTDir, eTTel ;
    DatabaseHelper helper = new DatabaseHelper(this);

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Datos del producto a registrar
        eTcodBarras = (EditText) findViewById(R.id.eTCodBarras);
        eTprecio = (EditText) findViewById(R.id.eTPrecio);
        eTNombre = (EditText) findViewById(R.id.eTNombre);
        eTMarca = (EditText) findViewById(R.id.eTMarca);
        eTExist = (EditText) findViewById(R.id.eTExist);
        eTProv = (EditText) findViewById(R.id.eTProv);
        eTDesc = (EditText) findViewById(R.id.eTDesc);
        // Datos del proveedor del producto
        eTNomProv = (EditText) findViewById(R.id.eTNProv);
        eTDir = (EditText) findViewById(R.id.eTDir);
        eTTel = (EditText) findViewById(R.id.eTTel);
        // Boton de registrar el producto
        final Button bRegistro = (Button) findViewById(R.id.bRegistrar);

        // Se regresa a la actividad donde el administrador modifica o agrega productos
        bRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validacion de datos para poder agregar el producto a la base de datos
                if(validarDatos() == true) {

                    String codBarras = eTcodBarras.getText().toString().trim();
                    float precio = Float.parseFloat(eTprecio.getText().toString());
                    String nombre = eTNombre.getText().toString().trim();
                    String marca = eTMarca.getText().toString();
                    int exist = Integer.parseInt(eTExist.getText().toString().trim());
                    String nProveedor = eTProv.getText().toString().trim();
                    String descrip = eTDesc.getText().toString();
                    String provNom = eTNomProv.getText().toString().trim();
                    String provDir = eTDir.getText().toString().trim();
                    String provTel = eTTel.getText().toString().trim();

                    // Se crea el proveedor con los datos introducidos
                    Proveedor proveedor = new Proveedor();
                    proveedor.setNombreP(provNom);
                    proveedor.setDireccion(provDir);
                    proveedor.setTelefono(provTel);

                   // Se crea el producto con los datos introducidos
                    Producto producto = new Producto();
                    producto.setCodigoBarra(codBarras);
                    producto.setPrecio(precio);
                    producto.setNombre(nombre);
                    producto.setMarca(marca);
                    producto.setExist(exist);
                    producto.setProveedor(proveedor);
                    producto.setDesc(provDir);
                    boolean isInDB = helper.buscarProducto(producto.getNombre());
                    // Si ya se encuentra el producto en la base de datos, se regresa a la ventana del admin
                    if(isInDB) {
                        Toast toast = Toast.makeText(AddProductActivity.this, "El producto ya existe, puedes elegir editarlo en EDITAR", Toast.LENGTH_SHORT);
                        toast.show();
                        Intent bRegistroIntent = new Intent(AddProductActivity.this, AdminEditActivity.class);
                        AddProductActivity.this.startActivity(bRegistroIntent);
                    }
                    // Si no, crea uno nuevo y se regresa a la ventana principal del admin
                    else {
                        helper.registrarProducto(producto);
                        Toast toast = Toast.makeText(AddProductActivity.this, "El producto ha sido agregado", Toast.LENGTH_SHORT);
                        toast.show();
                        Intent bRegistroIntent = new Intent(AddProductActivity.this, AdminEditActivity.class);
                        AddProductActivity.this.startActivity(bRegistroIntent);
                    }
                }
                else {
                    Toast toast = Toast.makeText(AddProductActivity.this, "Corrige los datos no validos", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    public boolean validarDatos() {
        String codBarras = eTcodBarras.getText().toString();
        if(Pattern.matches("^[^0-9]*$",codBarras)) {
            eTcodBarras.setError("El codigo de barras debe ser un numero!");
            return false;
        }

        try {
            float precio = Float.parseFloat(eTprecio.getText().toString());
        } catch(NumberFormatException e) {
            eTprecio.setError("El precio debe ser un numero");
            return false;
        }
        String nombre = eTNombre.getText().toString().trim();
        if (!Pattern.matches("^[\\p{L} .'-]+$",nombre)) {
            eTNombre.setError("El nombre no debe tener numeros");
            return false;
        }
        String marca = eTMarca.getText().toString();
        try {
            int exist = Integer.parseInt(eTExist.getText().toString().trim());
        } catch (NumberFormatException e) {
            eTExist.setError("El numero de productos debe ser un numero");
            return false;
        }
        String nProveedor = eTProv.getText().toString().trim();
        if (!Pattern.matches("^[^0-9]*$",nombre)) {
            eTProv.setError("El nombre del proveedor no debe tener numeros");
            return false;
        }
        String descrip = eTDesc.getText().toString();
        if (descrip.length() == 0) {
            eTDesc.setError("Este campo no puede estar vacio!");
            return false;
        }
        String provNom = eTNomProv.getText().toString().trim();
        if (provNom.length() == 0) {
            eTDesc.setError("Este campo no puede estar vacio!");
            return false;
        }
        String provDir = eTDir.getText().toString();
        if (provDir.length() == 0) {
            eTDesc.setError("Este campo no puede estar vacio!");
            return false;

        }
        String provTel = eTTel.getText().toString().trim();
        if (!PhoneNumberUtils.isGlobalPhoneNumber(provTel)) {
            eTTel.setError("El telefono debe tener numeros unicamente");
            return false;
        }
        return true;
    }
}
