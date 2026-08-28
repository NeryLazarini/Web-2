import java.util.concurrent.ConcurrentHashMap;

public class Respuesta5_2 {
    private ConcurrentHashMap<String, Registro> accesos = new ConcurrentHashMap<>();

    public boolean peticionPermitida(String ip) {
        long tiempoActual = System.currentTimeMillis() / 1000;
        accesos.putIfAbsent(ip, new Registro(tiempoActual, 0));
        Registro registro = accesos.get(ip);

        if (tiempoActual - registro.inicioVentana > 60) {
            registro.inicioVentana = tiempoActual;
            registro.contador = 1;
            return true;
        }

        if (registro.contador < 100) {
            registro.contador++;
            return true;
        }

        return false;
        // Aqui vamos a retornar un HTTP 429 Too Many Requests
    }

    private class Registro {
        long inicioVentana;
        int contador;
        Registro(long inicio, int cont) {
            this.inicioVentana = inicio;
            this.contador = cont;
        }
    }
}