/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NoteBlock;

/**
 *
 * @author andre
 */
public class Controla {
    
    public static int cont=0;
    String[ ] Contenedor = new String[100];
    public Controla(){
        Contenedor[cont++] = " ";
    }
    public void setString(String cadena){
        System.out.println(cadena);
        Contenedor[cont++] = cadena;
    }

    public String rehacer(){
        return Contenedor[++cont];
    }

    public String deshacer(){
        return Contenedor[--cont];
    }
}