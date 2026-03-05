package com.unimagdalena.servicio;

import com.poo.persistence.NioFile;
import com.unimagdalena.api.ApiOperacionBD;
import com.unimagdalena.controlador.genero.GeneroControladorListar;
import com.unimagdalena.dto.GeneroDto;
import com.unimagdalena.dto.PeliculaDto;
import com.unimagdalena.model.Genero;
import com.unimagdalena.model.Pelicula;
import com.unimagdalena.helpers.constante.Persistencia;
import com.unimagdalena.helpers.utilidad.GestorImagen;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PeliculaServicio implements ApiOperacionBD<PeliculaDto>{

    private NioFile miArchivo;
    private String nombrePersistencia;

    public PeliculaServicio() {
        try {
            nombrePersistencia = Persistencia.NOMBRE_PELICULA;
            miArchivo = new NioFile(nombrePersistencia);
        } catch (IOException ex) {
            Logger.getLogger(PeliculaServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public List<PeliculaDto> SelectFrom() {
        List<PeliculaDto> arregloPeliculas = new ArrayList<>();
        List<String> arregloDatos = miArchivo.obtenerDatos();
        
        for (String cadena : arregloDatos) {
            try {
                cadena = cadena.replace("@", "");
                String[] columnas = cadena.split(Persistencia.SEPARADOR_COLUMNAS);
                
                int codPelicula = Integer.parseInt(columnas[0].trim());
                String nombrePelicula = columnas[1].trim();
                LocalDate fechaestrenoPelicula = LocalDate.parse(columnas[2].trim());
                GeneroDto Generopeli = null;
                for (GeneroDto obj : GeneroControladorListar.arregloGeneros()) {
                    if (obj.getIdGenero() == Integer.parseInt(columnas[3].trim())) {
                        Generopeli = obj;
                    }
                }
                
                Boolean es3dPelicula = Boolean.valueOf(columnas[4].trim());
                String edadPelicula = columnas[5].trim();
                String imagPublicas = columnas[6].trim();
                String imagPrivadas = columnas[7].trim();
                
                arregloPeliculas.add(new PeliculaDto(codPelicula, nombrePelicula, fechaestrenoPelicula, Generopeli, es3dPelicula, edadPelicula, imagPublicas, imagPrivadas));
                                        
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }
        return arregloPeliculas;
    }

    public Map<Integer, Integer> selectFromCantidad() {
        Map<Integer, Integer> arrCantidades = new HashMap<>();
        List<String> arregloDatos = miArchivo.obtenerDatos();

        for (String cadena : arregloDatos) {
            try {
                cadena = cadena.replace("@", "");
                String[] columnas = cadena.split(Persistencia.SEPARADOR_COLUMNAS);
                int idGenero = Integer.parseInt(columnas[3].trim());
                arrCantidades.put(idGenero, arrCantidades.getOrDefault(idGenero, 0) + 1);

            } catch (NumberFormatException error) {
                Logger.getLogger(PeliculaServicio.class.getName()).log(Level.SEVERE, null, error);
            }
        }
        return arrCantidades;
    }
    
    @Override
    public PeliculaDto insertInto(PeliculaDto dto, String ruta) {
        Pelicula objPelicula = new Pelicula();
        Genero objGenero = new Genero(
                dto.getIdGeneroPelicula().getIdGenero(),
                dto.getIdGeneroPelicula().getNombreGenero(),
                dto.getIdGeneroPelicula().getEstadoGenero(),
                dto.getIdGeneroPelicula().getCantPeliculasGenero(),
                dto.getIdGeneroPelicula().getCalificacionGenero(),
                dto.getIdGeneroPelicula().getFechaCreacionGenero(),
                dto.getIdGeneroPelicula().getNombreImagenPublicoGenero(),
                dto.getIdGeneroPelicula().getNombreImagenPrivadoGenero(),
                dto.getIdGeneroPelicula().getPublicoObjetivoGenero()
        );
        
        objPelicula.setIdPelicula(getSerial());
        objPelicula.setNombrePelicula(dto.getNombrePelicula());
        objPelicula.setFechaEstrenoPelicula(dto.getFechaEstrenoPelicula());
        objPelicula.setIdGeneroPelicula(objGenero);
        objPelicula.setEs3dPelicula(dto.getEs3dPelicula());
        objPelicula.setClasificacionEdadPelicula(dto.getClasificacionEdadPelicula());
        objPelicula.setNombreImagenPublicoPelicula(dto.getNombreImagenPublicoPelicula());
        objPelicula.setNombreImagenPrivadoPelicula(GestorImagen.grabarLaImagen(ruta));
        
        String cadena 
                = objPelicula.getIdPelicula() + Persistencia.SEPARADOR_COLUMNAS
                + objPelicula.getNombrePelicula() + Persistencia.SEPARADOR_COLUMNAS
                + objPelicula.getFechaEstrenoPelicula() + Persistencia.SEPARADOR_COLUMNAS
                + objPelicula.getIdGeneroPelicula().getIdGenero() + Persistencia.SEPARADOR_COLUMNAS
                + objPelicula.getEs3dPelicula() + Persistencia.SEPARADOR_COLUMNAS
                + objPelicula.getClasificacionEdadPelicula() + Persistencia.SEPARADOR_COLUMNAS
                + objPelicula.getNombreImagenPublicoPelicula() + Persistencia.SEPARADOR_COLUMNAS
                + objPelicula.getNombreImagenPrivadoPelicula();
        
        if (miArchivo.agregarRegistro(cadena)) {
            dto.setIdPelicula(objPelicula.getIdPelicula());
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
            Logger.getLogger(PeliculaServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return codigo;
    }

    @Override
    public int numRows() {
        int cantidad = 0;
        try {
            cantidad = miArchivo.cantidadFilas();
        } catch (IOException ex) {
            Logger.getLogger(PeliculaServicio.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(PeliculaServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return correcto;
    }

    @Override
    public Boolean updateSet(int indice, PeliculaDto obj, String ruta) {
        boolean correcto = false;
        try {
            String cadena, nocu;
            
            cadena =
                    obj.getIdPelicula() + Persistencia.SEPARADOR_COLUMNAS +
                    obj.getNombrePelicula() + Persistencia.SEPARADOR_COLUMNAS +
                    obj.getFechaEstrenoPelicula() + Persistencia.SEPARADOR_COLUMNAS +
                    obj.getIdGeneroPelicula().getIdGenero() + Persistencia.SEPARADOR_COLUMNAS +
                    obj.getEs3dPelicula() + Persistencia.SEPARADOR_COLUMNAS +
                    obj.getClasificacionEdadPelicula() + Persistencia.SEPARADOR_COLUMNAS +
                    obj.getNombreImagenPublicoPelicula() + Persistencia.SEPARADOR_COLUMNAS;

            if (ruta.isBlank()) {
                cadena = cadena + obj.getNombreImagenPrivadoPelicula();
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
            Logger.getLogger(PeliculaServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return correcto;
    }

    @Override
    public PeliculaDto getOne(int indice) {
        PeliculaDto objListo = null;
        try {
            List<String> arrDatos = miArchivo.obtenerFila(indice);
            if (arrDatos != null && arrDatos.size() >= 8) {
                
                int codPelicula = Integer.parseInt(arrDatos.get(0).trim());
                String nombrePelicula = arrDatos.get(1).trim();
                LocalDate fechaestrenoPelicula = LocalDate.parse(arrDatos.get(2).trim());
                int idGenero = Integer.parseInt(arrDatos.get(3).trim());
                Boolean es3dPelicula = Boolean.valueOf(arrDatos.get(4).trim());
                String edadPelicula = arrDatos.get(5).trim();
                String imagPublicas = arrDatos.get(6).trim();
                String imagPrivadas = arrDatos.get(7).trim();
                
                GeneroDto Generopeli = null;
                for (GeneroDto obj : GeneroControladorListar.arregloGeneros()) {
                    if (obj.getIdGenero() == idGenero) {
                        Generopeli = obj;
                        break;
                    }
                }
                
                if (Generopeli != null) {
                    objListo = new PeliculaDto(codPelicula, nombrePelicula, fechaestrenoPelicula, Generopeli, es3dPelicula, edadPelicula, imagPublicas, imagPrivadas);
                }
            }
        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(PeliculaServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return objListo;
    }
    
}