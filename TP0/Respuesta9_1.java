public Perfil obtenerPerfil(String id) {
    String clave = "perfil_" + id;
    Perfil perfil = cache.obtener(clave); 
    
    if (perfil != null) {
        return perfil; 
    }
    
    perfil = baseDeDatos.ejecutarConsulta("SELECT * FROM perfiles WHERE id = ?", id);
    
    if (perfil != null) {
        cache.guardar(clave, perfil, 3600); 
    }
    
    return perfil;
}

public void actualizarPerfil(String id, Perfil nuevosDatos) {
    baseDeDatos.ejecutarUpdate("UPDATE perfiles SET ... WHERE id = ?", id, nuevosDatos);
    cache.invalidar("perfil_" + id); 
}