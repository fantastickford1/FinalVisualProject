/**
 * Gestion de buffer de distintas verisones de un string,
 * Controla rehacer y deshacer
 * @author Flyzx
 * @version 1.5
 */

package NoteBlock;

public class Version {

    private Version anterior;
    private String texto;
    private Version siguiente;

    //Constructor por defecto.
    public Version() {
        anterior  = null;
        texto     = null;
        siguiente = null;
    }

    //Constructor con nodo
    public Version(String texto) {
        this();
        this.texto = texto;
    }

    /**
     * @param anterior : Tipo Version Agrega Nodo que contiene infomación 
     *                   del string anterior
     */
    public void setAnterior(Version anterior) {
        this.anterior = anterior;
    }

    /**
     * @param siguiente : Tipo Version Agrega Nodo que contiene infomación 
     *                   del string siguiente
     */
    public void setSiguiente(Version siguiente) {
        this.siguiente = siguiente;
        this.siguiente.setAnterior(this);
    }

    /**
     * @return anterior : de tipo Version regresa la version anterior del nodo
     */
    public Version getAnterior() {
        return anterior;
    }

    /**
     * @return siguiente : de tipo Version regresa la version siguiente del nodo
     */
    public Version getSiguiente() {
        return siguiente;
    }

    /**
     * @return texto : retorna tipo String Obtiene el texto de la 
     *                  version del nodo actual.
     */
    public String getTexto() {
        return texto;
    }
}
