package com.unimagdalena.servicio;

import com.poo.persistence.NioFile;
import com.unimagdalena.api.ApiOperacionBD;
import com.unimagdalena.controlador.cliente.ClienteControladorListar;
import com.unimagdalena.controlador.funcion.FuncionControladorListar;
import com.unimagdalena.dto.ClienteDto;
import com.unimagdalena.dto.FuncionDto;
import com.unimagdalena.dto.ReservaDto;
import com.unimagdalena.model.Cliente;
import com.unimagdalena.model.Funcion;
import com.unimagdalena.model.Reserva;
import com.unimagdalena.helpers.constante.Persistencia;
import com.unimagdalena.helpers.utilidad.GestorImagen;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReservaServicio implements ApiOperacionBD<ReservaDto> {

    private NioFile miArchivo;
    private String nombrePersistencia;

    public ReservaServicio() {
        try {
            nombrePersistencia = Persistencia.NOMBRE_RESERVA;
            miArchivo = new NioFile(nombrePersistencia);
        } catch (IOException ex) {
            Logger.getLogger(ReservaServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<ReservaDto> SelectFrom() {
        List<ReservaDto> arregloReservas = new ArrayList<>();
        List<String> arregloDatos = miArchivo.obtenerDatos();

        List<ClienteDto> clientes = ClienteControladorListar.obtenerTodos();
        List<FuncionDto> funciones = FuncionControladorListar.arregloFunciones();

        for (String cadena : arregloDatos) {
            try {
                cadena = cadena.replace("@", "");
                String[] columnas = cadena.split(Persistencia.SEPARADOR_COLUMNAS);

                // Lectura con nuevo campo: id, nombre, metodo, fecha, cliente_id, funcion_id, bebidas, img_pub, img_priv
                int idReserva = Integer.parseInt(columnas[0].trim());
                String nombreReserva = columnas[1].trim(); 
                String metodoPago = columnas[2].trim(); 
                LocalDate fechaReserva = LocalDate.parse(columnas[3].trim());
                int idCliente = Integer.parseInt(columnas[4].trim());
                int idFuncion = Integer.parseInt(columnas[5].trim());
                Boolean incluyeBebidas = Boolean.valueOf(columnas[6].trim());
                String imagPublicas = columnas[7].trim();
                String imagPrivadas = columnas[8].trim();

                ClienteDto clienteReserva = clientes.stream()
                        .filter(c -> c.getIdCliente() == idCliente)
                        .findFirst().orElse(null);

                FuncionDto funcionReserva = funciones.stream()
                        .filter(f -> f.getIdFuncion() == idFuncion)
                        .findFirst().orElse(null);

                if (clienteReserva != null && funcionReserva != null) {
                    arregloReservas.add(new ReservaDto(idReserva, nombreReserva, metodoPago, fechaReserva, clienteReserva, funcionReserva, incluyeBebidas, imagPublicas, imagPrivadas));
                }

            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }
        return arregloReservas;
    }

    @Override
    public ReservaDto insertInto(ReservaDto dto, String ruta) {
        Reserva objReserva = new Reserva();

        Cliente cliente = new Cliente();
        cliente.setIdCliente(dto.getIdClienteReserva().getIdCliente());

        Funcion funcion = new Funcion();
        funcion.setIdFuncion(dto.getIdFuncionReserva().getIdFuncion());

        objReserva.setIdReserva(getSerial());
        objReserva.setNombreReserva(dto.getNombreReserva()); 
        objReserva.setMetodoPagoReserva(dto.getMetodoPagoReserva());
        objReserva.setFechaReserva(dto.getFechaReserva());
        objReserva.setIdClienteReserva(cliente);
        objReserva.setIdFuncionReserva(funcion);
        objReserva.setIncluyeBebidasReserva(dto.getIncluyeBebidasReserva());
        objReserva.setNombreImagenPublicoReserva(dto.getNombreImagenPublicoReserva());
        objReserva.setNombreImagenPrivadoReserva(GestorImagen.grabarLaImagen(ruta));

        // Escritura con nuevo campo: id, nombre, metodo, fecha, cliente_id, funcion_id, bebidas, img_pub, img_priv
        String cadena
                = objReserva.getIdReserva() + Persistencia.SEPARADOR_COLUMNAS
                + objReserva.getNombreReserva() + Persistencia.SEPARADOR_COLUMNAS 
                + objReserva.getMetodoPagoReserva() + Persistencia.SEPARADOR_COLUMNAS 
                + objReserva.getFechaReserva() + Persistencia.SEPARADOR_COLUMNAS
                + objReserva.getIdClienteReserva().getIdCliente() + Persistencia.SEPARADOR_COLUMNAS
                + objReserva.getIdFuncionReserva().getIdFuncion() + Persistencia.SEPARADOR_COLUMNAS
                + objReserva.getIncluyeBebidasReserva() + Persistencia.SEPARADOR_COLUMNAS
                + objReserva.getNombreImagenPublicoReserva() + Persistencia.SEPARADOR_COLUMNAS
                + objReserva.getNombreImagenPrivadoReserva();

        if (miArchivo.agregarRegistro(cadena)) {
            dto.setIdReserva(objReserva.getIdReserva());
            return dto;
        }

        return null;
    }
    
    @Override
    public Boolean updateSet(int indice, ReservaDto obj, String ruta) {
        boolean correcto = false;

        try {
            String cadena, nocu;

            // Escritura con nuevo campo: id, nombre, metodo, fecha, cliente_id, funcion_id, bebidas, img_pub,
            cadena = obj.getIdReserva() + Persistencia.SEPARADOR_COLUMNAS
                    + obj.getNombreReserva() + Persistencia.SEPARADOR_COLUMNAS 
                    + obj.getMetodoPagoReserva() + Persistencia.SEPARADOR_COLUMNAS 
                    + obj.getFechaReserva() + Persistencia.SEPARADOR_COLUMNAS
                    + obj.getIdClienteReserva().getIdCliente() + Persistencia.SEPARADOR_COLUMNAS
                    + obj.getIdFuncionReserva().getIdFuncion() + Persistencia.SEPARADOR_COLUMNAS
                    + obj.getIncluyeBebidasReserva() + Persistencia.SEPARADOR_COLUMNAS
                    + obj.getNombreImagenPublicoReserva() + Persistencia.SEPARADOR_COLUMNAS;

            if (ruta.isBlank()) {
                cadena = cadena + obj.getNombreImagenPrivadoReserva();
            } else {
                nocu = GestorImagen.grabarLaImagen(ruta);
                cadena = cadena + nocu;

                List<String> oldData = miArchivo.obtenerFila(indice);
                if (oldData != null && oldData.size() >= 8) {
                    String oldImgPriv = oldData.get(7).trim();
                    if (oldImgPriv != null && !oldImgPriv.isEmpty()) {
                        String nombreBorrar = Persistencia.RUTA_IMAGENES_EXTERNAS + Persistencia.SEPARADOR_CARPETAS + oldImgPriv;
                        Path rutaBorrar = Paths.get(nombreBorrar);
                        Files.deleteIfExists(rutaBorrar);
                    }
                }
            }

            if (miArchivo.actualizaFilaPosicion(indice, cadena)) {
                correcto = true;
            }

        } catch (IOException e) {
            Logger.getLogger(ReservaServicio.class.getName()).log(Level.SEVERE, null, e);
        }
        return correcto;
    }
    
    @Override
    public ReservaDto getOne(int indice) {
        ReservaDto objListo = null;
        try {
            List<String> arrDatos = miArchivo.obtenerFila(indice);
            if (arrDatos != null && arrDatos.size() >= 9) {
                
                int idReserva = Integer.parseInt(arrDatos.get(0).trim());
                String nombreReserva = arrDatos.get(1).trim(); 
                String metodoPago = arrDatos.get(2).trim(); 
                LocalDate fechaReserva = LocalDate.parse(arrDatos.get(3).trim());
                int idCliente = Integer.parseInt(arrDatos.get(4).trim());
                int idFuncion = Integer.parseInt(arrDatos.get(5).trim());
                Boolean incluyeBebidas = Boolean.valueOf(arrDatos.get(6).trim());
                String imagPublicas = arrDatos.get(7).trim();
                String imagPrivadas = arrDatos.get(8).trim();

                List<ClienteDto> clientes = ClienteControladorListar.obtenerTodos();
                List<FuncionDto> funciones = FuncionControladorListar.arregloFunciones();

                ClienteDto clienteReserva = clientes.stream()
                        .filter(c -> c.getIdCliente() == idCliente)
                        .findFirst().orElse(null);

                FuncionDto funcionReserva = funciones.stream()
                        .filter(f -> f.getIdFuncion() == idFuncion)
                        .findFirst().orElse(null);

                if (clienteReserva != null && funcionReserva != null) {
                    objListo = new ReservaDto(idReserva, nombreReserva, metodoPago, fechaReserva, clienteReserva, funcionReserva, incluyeBebidas, imagPublicas, imagPrivadas);
                }
            }
        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(ReservaServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return objListo;
    }

    // Métodos getSerial, numRows y deleteFrom no necesitan cambios.
    @Override
    public int getSerial() {
        int codigo = 0;
        try {
            codigo = miArchivo.ultimoCodigo() + 1;
        } catch (IOException ex) {
            Logger.getLogger(ReservaServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return codigo;
    }

    @Override
    public int numRows() {
        int cantidad = 0;
        try {
            cantidad = miArchivo.cantidadFilas();
        } catch (IOException ex) {
            Logger.getLogger(ReservaServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cantidad;
    }

    @Override
    public Boolean deleteFrom(int indice) {
        Boolean correcto = false;
        try {
            List<String> arregloDeDatos;
            arregloDeDatos = miArchivo.borrarFilaPosicion(indice);
            if (!arregloDeDatos.isEmpty()) {
                correcto = true;
            }
        } catch (IOException ex) {
            Logger.getLogger(ReservaServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return correcto;
    }
}