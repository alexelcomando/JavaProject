package com.unimagdalena.servicio;

import com.poo.persistence.NioFile;
import com.unimagdalena.api.ApiOperacionBD;
import com.unimagdalena.controlador.sala.SalaControladorListar;
import com.unimagdalena.dto.EmpleadoDto;
import com.unimagdalena.dto.SalaDto;
import com.unimagdalena.model.Empleado;
import com.unimagdalena.model.Sala;
import com.unimagdalena.helpers.constante.Persistencia;
import com.unimagdalena.helpers.utilidad.GestorImagen;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmpleadoServicio implements ApiOperacionBD<EmpleadoDto> {
    
    private NioFile miArchivo;
    private String nombrePersistencia;

    public EmpleadoServicio() {
        try {
            nombrePersistencia = Persistencia.NOMBRE_EMPLEADO;
            miArchivo = new NioFile(nombrePersistencia);
        } catch (IOException ex) {
            Logger.getLogger(EmpleadoServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public List<EmpleadoDto> SelectFrom() {
        List<EmpleadoDto> arregloEmpleados = new ArrayList<>();
        List<String> arregloDatos = miArchivo.obtenerDatos();
        
        for (String cadena : arregloDatos) {
            try {
                cadena = cadena.replace("@", "");
                String[] columnas = cadena.split(Persistencia.SEPARADOR_COLUMNAS);
                
                int codEmpleado = Integer.parseInt(columnas[0].trim());
                String nombreEmpleado = columnas[1].trim();
                Boolean esTiempoCompEmpleado = Boolean.valueOf(columnas[2].trim());
                String nivelAccesoEmpleado = columnas[3].trim();
                SalaDto salaEmpleado = null;
                for (SalaDto obj : SalaControladorListar.arregloSalas()) {
                    if (obj.getIdSala() == Integer.parseInt(columnas[4].trim())) {
                        salaEmpleado = obj;
                    }
                }
                LocalTime entradaEmpleado = LocalTime.parse(columnas[5].trim());
                LocalTime salidaEmpleado = LocalTime.parse(columnas[6].trim());
                String imagPublicas = columnas[7].trim();
                String imagPrivadas = columnas[8].trim();
                
                arregloEmpleados.add(new EmpleadoDto(codEmpleado, nombreEmpleado, esTiempoCompEmpleado, nivelAccesoEmpleado, salaEmpleado, entradaEmpleado, salidaEmpleado, imagPublicas, imagPrivadas));
                
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }
        return arregloEmpleados;
    }

    @Override
    public EmpleadoDto insertInto(EmpleadoDto dto, String ruta) {
        Empleado objEmpleado = new Empleado();
        Sala objSala = new Sala(
                dto.getSalaAsignadaEmpleado().getIdSala(),
                dto.getSalaAsignadaEmpleado().getNombreSala(),
                dto.getSalaAsignadaEmpleado().getIdPeliculaSala(),
                dto.getSalaAsignadaEmpleado().getEstaDisponibleSala(),
                dto.getSalaAsignadaEmpleado().getNumeroAsientosSala(),
                dto.getSalaAsignadaEmpleado().getServicioVipSala(),
                dto.getSalaAsignadaEmpleado().getNombreImagenPublicoSala(),
                dto.getSalaAsignadaEmpleado().getNombreImagenPrivadoSala()
        );
        
        objEmpleado.setIdEmpleado(getSerial());
        objEmpleado.setNombreEmpleado(dto.getNombreEmpleado());
        objEmpleado.setEsTiempoCompletoEmpleado(dto.getEsTiempoCompletoEmpleado());
        objEmpleado.setNivelAccesoEmpleado(dto.getNivelAccesoEmpleado());
        objEmpleado.setSalaAsignadaEmpleado(objSala);
        objEmpleado.setHoraEntradaEmpleado(dto.getHoraEntradaEmpleado());
        objEmpleado.setHoraSalidaEmpleado(dto.getHoraSalidaEmpleado());
        objEmpleado.setNombreImagenPublicoEmpleado(dto.getNombreImagenPublicoEmpleado());
        objEmpleado.setNombreImagenPrivadoEmpleado(GestorImagen.grabarLaImagen(ruta));
        
        String cadena
                = objEmpleado.getIdEmpleado() + Persistencia.SEPARADOR_COLUMNAS
                + objEmpleado.getNombreEmpleado() + Persistencia.SEPARADOR_COLUMNAS
                + objEmpleado.getEsTiempoCompletoEmpleado() + Persistencia.SEPARADOR_COLUMNAS
                + objEmpleado.getNivelAccesoEmpleado() + Persistencia.SEPARADOR_COLUMNAS
                + objEmpleado.getSalaAsignadaEmpleado().getIdSala() + Persistencia.SEPARADOR_COLUMNAS
                + objEmpleado.getHoraEntradaEmpleado() + Persistencia.SEPARADOR_COLUMNAS
                + objEmpleado.getHoraSalidaEmpleado() + Persistencia.SEPARADOR_COLUMNAS
                + objEmpleado.getNombreImagenPublicoEmpleado() + Persistencia.SEPARADOR_COLUMNAS
                + objEmpleado.getNombreImagenPrivadoEmpleado();
        
        if (miArchivo.agregarRegistro(cadena)) {
            dto.setIdEmpleado(objEmpleado.getIdEmpleado());
            return dto;
        }
        
        return null;
        
    }

    @Override
    public int getSerial() {
        int codigo = 0;
        try {
            codigo = miArchivo.ultimoCodigo() + 1;
        } catch (IOException ex) {
            Logger.getLogger(EmpleadoServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return codigo;
    }

    @Override
    public int numRows() {
        int cantidad = 0;
        try {
            cantidad = miArchivo.cantidadFilas();
        } catch (IOException ex) {
            Logger.getLogger(EmpleadoServicio.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(EmpleadoServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return correcto;
    }

    @Override
    public Boolean updateSet(int indice, EmpleadoDto obj, String ruta) {
        boolean correcto = false;
        try {
            String cadena, nocu;
            
            cadena =
                    obj.getIdEmpleado() + Persistencia.SEPARADOR_COLUMNAS +
                    obj.getNombreEmpleado() + Persistencia.SEPARADOR_COLUMNAS +
                    obj.getEsTiempoCompletoEmpleado() + Persistencia.SEPARADOR_COLUMNAS +
                    obj.getNivelAccesoEmpleado() + Persistencia.SEPARADOR_COLUMNAS +
                    obj.getSalaAsignadaEmpleado().getIdSala() + Persistencia.SEPARADOR_COLUMNAS +
                    obj.getHoraEntradaEmpleado() + Persistencia.SEPARADOR_COLUMNAS +
                    obj.getHoraSalidaEmpleado() + Persistencia.SEPARADOR_COLUMNAS +
                    obj.getNombreImagenPublicoEmpleado() + Persistencia.SEPARADOR_COLUMNAS;
            
            if (ruta.isBlank()) {
                cadena = cadena + obj.getNombreImagenPrivadoEmpleado();
            } else {
                nocu = GestorImagen.grabarLaImagen(ruta);
                cadena = cadena + nocu;
                
                List<String> oldData = miArchivo.obtenerFila(indice);
                if (oldData != null && oldData.size() >= 9) {
                    String oldImgPriv = oldData.get(8).trim();
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
  
        } catch (IOException ex) {
            Logger.getLogger(EmpleadoServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return correcto;
    }

    @Override
    public EmpleadoDto getOne(int indice) {
        EmpleadoDto objListo = null;
        try {
            List<String> arrDatos = miArchivo.obtenerFila(indice);
            if (arrDatos != null && arrDatos.size() >= 9) {
                
                int codEmpleado = Integer.parseInt(arrDatos.get(0).trim());
                String nombreEmpleado = arrDatos.get(1).trim();
                Boolean esTiempoCompEmpleado = Boolean.valueOf(arrDatos.get(2).trim());
                String nivelAccesoEmpleado = arrDatos.get(3).trim();
                int idSala = Integer.parseInt(arrDatos.get(4).trim());
                LocalTime entradaEmpleado = LocalTime.parse(arrDatos.get(5).trim());
                LocalTime salidaEmpleado = LocalTime.parse(arrDatos.get(6).trim());
                String imagPublicas = arrDatos.get(7).trim();
                String imagPrivadas = arrDatos.get(8).trim();
                
                SalaDto salaEmpleado = null;
                for (SalaDto obj : SalaControladorListar.arregloSalas()) {
                    if (obj.getIdSala() == idSala) {
                        salaEmpleado = obj;
                        break;
                    }
                }
                
                if (salaEmpleado != null) {
                    objListo = new EmpleadoDto(codEmpleado, nombreEmpleado, esTiempoCompEmpleado, nivelAccesoEmpleado, salaEmpleado, entradaEmpleado, salidaEmpleado, imagPublicas, imagPrivadas);
                }
            }
                     
        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(EmpleadoServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return objListo;
    }
    
}
