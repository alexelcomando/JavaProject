package com.unimagdalena.servicio;

import com.poo.persistence.NioFile;
import com.unimagdalena.api.ApiOperacionBD;
import com.unimagdalena.dto.GeneroDto;
import com.unimagdalena.model.Genero;
import com.unimagdalena.helpers.constante.Persistencia;
import com.unimagdalena.helpers.utilidad.GestorImagen;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GeneroServicio implements ApiOperacionBD<GeneroDto> {

    private NioFile miArchivo;
    private String nombrePersistencia;

    public GeneroServicio() {
        try {
            nombrePersistencia = Persistencia.NOMBRE_GENERO;
            miArchivo = new NioFile(nombrePersistencia);
        } catch (IOException ex) {
            Logger.getLogger(GeneroServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public List<GeneroDto> SelectFrom() {
        List<GeneroDto> arregloGenero = new ArrayList<>();
        List<String> arregloDatos = miArchivo.obtenerDatos();

        PeliculaServicio peliculaServicio = new PeliculaServicio();
        Map<Integer, Integer> arrCantPelis = peliculaServicio.selectFromCantidad();

        for (String cadena : arregloDatos) {
            try {
                cadena = cadena.replace("@", "");
                String[] columnas = cadena.split(Persistencia.SEPARADOR_COLUMNAS);
                
                if (columnas.length < 8) continue; 

                int codGenero = Integer.parseInt(columnas[0].trim());
                String nomGenero = columnas[1].trim();
                Boolean estGenero = Boolean.valueOf(columnas[2].trim());
                int calificacion = Integer.parseInt(columnas[3].trim());
                LocalDate fechaCreacion = LocalDate.parse(columnas[4].trim());
                String publicoObj = columnas[5].trim();
                String imgPub = columnas[6].trim();
                String imgPriv = columnas[7].trim();

                int cantPelis = arrCantPelis.getOrDefault(codGenero, 0);
        
                arregloGenero.add(new GeneroDto(
                        codGenero, nomGenero, estGenero, cantPelis, calificacion, fechaCreacion, publicoObj, imgPub, imgPriv));

            } catch (NumberFormatException e) {
                System.out.println("Error al parsear datos: " + e.getMessage());
            }
        }
        return arregloGenero;

    }

    public List<GeneroDto> selectFromWhereEstadoTrue() {
        List<GeneroDto> arregloGenero = new ArrayList<>();
        List<String> arregloDatos = miArchivo.obtenerDatos();

        for (String cadena : arregloDatos) {
            try {
                cadena = cadena.replace("@", "");
                String[] columnas = cadena.split(Persistencia.SEPARADOR_COLUMNAS);
                
                if (columnas.length < 8) continue;
                
                int codGenero = Integer.parseInt(columnas[0].trim());
                String nomGenero = columnas[1].trim();
                Boolean estGenero = Boolean.valueOf(columnas[2].trim());
                int calificacion = Integer.parseInt(columnas[3].trim());
                LocalDate fechaCreacion = LocalDate.parse(columnas[4].trim());
                String publicoObj = columnas[5].trim();
                String imgPub = columnas[6].trim();
                String imgPriv = columnas[7].trim();
                
                if (Boolean.TRUE.equals(estGenero)) {
                    arregloGenero.add(new GeneroDto(
                            codGenero, nomGenero, estGenero, 0, calificacion, fechaCreacion, publicoObj, imgPub, imgPriv));
                }

            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }
        return arregloGenero;
    }
    
    @Override
    public GeneroDto insertInto(GeneroDto dto, String ruta) {
        Genero ObjGenero = new Genero();
        ObjGenero.setIdGenero(getSerial());
        ObjGenero.setNombreGenero(dto.getNombreGenero());
        ObjGenero.setEstadoGenero(dto.getEstadoGenero());
        ObjGenero.setCantPeliculasGenero(0); 
        ObjGenero.setCalificacionGenero(dto.getCalificacionGenero());
        ObjGenero.setFechaCreacionGenero(dto.getFechaCreacionGenero());
        ObjGenero.setPublicoObjetivoGenero(dto.getPublicoObjetivoGenero());

        ObjGenero.setNombreImagenPublicoGenero(dto.getNombreImagenPublicoGenero());
        ObjGenero.setNombreImagenPrivadoGenero(GestorImagen.grabarLaImagen(ruta));

        String cadena = ObjGenero.getIdGenero() + Persistencia.SEPARADOR_COLUMNAS
                + ObjGenero.getNombreGenero() + Persistencia.SEPARADOR_COLUMNAS
                + ObjGenero.getEstadoGenero() + Persistencia.SEPARADOR_COLUMNAS
                + ObjGenero.getCalificacionGenero() + Persistencia.SEPARADOR_COLUMNAS
                + ObjGenero.getFechaCreacionGenero() + Persistencia.SEPARADOR_COLUMNAS
                + ObjGenero.getPublicoObjetivoGenero() + Persistencia.SEPARADOR_COLUMNAS
                + ObjGenero.getNombreImagenPublicoGenero() + Persistencia.SEPARADOR_COLUMNAS
                + ObjGenero.getNombreImagenPrivadoGenero();

        if (miArchivo.agregarRegistro(cadena)) {
            dto.setIdGenero(ObjGenero.getIdGenero());
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
            Logger.getLogger(GeneroServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return codigo;
    }

    @Override
    public int numRows() {
        int cantidad = 0;
        try {
            cantidad = miArchivo.cantidadFilas();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return cantidad;
    }

    @Override
    public Boolean deleteFrom(int indice) {
        try {
            List<String> arregloDatos = miArchivo.obtenerDatos();
            if (indice >= 0 && indice < arregloDatos.size()) {
                miArchivo.borrarFilaPosicion(indice);
                return true;
            }
        } catch (Exception ex) {
            Logger.getLogger(GeneroServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }


    @Override
    public Boolean updateSet(int indice, GeneroDto obj, String ruta) {
        boolean correcto = false;
        try {
            String cadena, nocu;

            cadena = obj.getIdGenero() + Persistencia.SEPARADOR_COLUMNAS
                    + obj.getNombreGenero() + Persistencia.SEPARADOR_COLUMNAS
                    + obj.getEstadoGenero() + Persistencia.SEPARADOR_COLUMNAS
                    + obj.getCalificacionGenero() + Persistencia.SEPARADOR_COLUMNAS
                    + obj.getFechaCreacionGenero() + Persistencia.SEPARADOR_COLUMNAS
                    + obj.getPublicoObjetivoGenero() + Persistencia.SEPARADOR_COLUMNAS
                    + obj.getNombreImagenPublicoGenero() + Persistencia.SEPARADOR_COLUMNAS;

            if (ruta.isBlank()) {
                cadena = cadena + obj.getNombreImagenPrivadoGenero();
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
        } catch (IOException ex) {
            Logger.getLogger(GeneroServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return correcto;
    }

    @Override
    public GeneroDto getOne(int indice) {
        GeneroDto objListo = null;
        try {
            List<String> columnas = miArchivo.obtenerFila(indice);
            
            if (columnas != null && columnas.size() >= 8) {
                
                int codGenero = Integer.parseInt(columnas.get(0).trim());
                String nomGenero = columnas.get(1).trim();
                Boolean estGenero = Boolean.valueOf(columnas.get(2).trim());
                int calificacion = Integer.parseInt(columnas.get(3).trim());
                LocalDate fechaCreacion = LocalDate.parse(columnas.get(4).trim());
                String publicoObj = columnas.get(5).trim();
                String imgPub = columnas.get(6).trim();
                String imgPriv = columnas.get(7).trim();

                PeliculaServicio peliculaServicio = new PeliculaServicio();
                Map<Integer, Integer> arrCantPelis = peliculaServicio.selectFromCantidad();
                int cantPelis = arrCantPelis.getOrDefault(codGenero, 0);

                objListo = new GeneroDto(
                        codGenero, nomGenero, estGenero, cantPelis, calificacion, fechaCreacion, publicoObj, imgPub, imgPriv);
            }
        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(GeneroServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return objListo;
    }
}