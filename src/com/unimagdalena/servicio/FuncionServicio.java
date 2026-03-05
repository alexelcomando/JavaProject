package com.unimagdalena.servicio;

import com.poo.persistence.NioFile;
import com.unimagdalena.api.ApiOperacionBD;
import com.unimagdalena.controlador.pelicula.PeliculaControladorListar;
import com.unimagdalena.controlador.sala.SalaControladorListar;
import com.unimagdalena.dto.FuncionDto;
import com.unimagdalena.dto.GeneroDto;
import com.unimagdalena.dto.PeliculaDto;
import com.unimagdalena.dto.SalaDto;
import com.unimagdalena.model.Funcion;
import com.unimagdalena.model.Genero;
import com.unimagdalena.model.Pelicula;
import com.unimagdalena.model.Sala;
import com.unimagdalena.helpers.constante.Persistencia;
import com.unimagdalena.helpers.utilidad.GestorImagen;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FuncionServicio implements ApiOperacionBD<FuncionDto> {

    private NioFile miArchivo;
    private String nombrePersistencia;

    public FuncionServicio() {
        try {
            nombrePersistencia = Persistencia.NOMBRE_FUNCIONE;
            miArchivo = new NioFile(nombrePersistencia);
        } catch (IOException ex) {
            Logger.getLogger(FuncionServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<FuncionDto> SelectFrom() {
        List<FuncionDto> arregloFunciones = new ArrayList<>();
        List<String> arregloDatos = miArchivo.obtenerDatos();

        for (String cadena : arregloDatos) {
            try {
                cadena = cadena.replace("@", "");
                String[] columnas = cadena.split(Persistencia.SEPARADOR_COLUMNAS);

                int codfuncion = Integer.parseInt(columnas[0].trim());
                String nombrefuncion = columnas[1].trim();
                PeliculaDto pelifuncion = null;
                for (PeliculaDto objp : PeliculaControladorListar.arregloPeliculas()) {
                    if (objp.getIdPelicula() == Integer.parseInt(columnas[2].trim())) {
                        pelifuncion = objp;
                    }
                }
                SalaDto salafuncion = null;
                for (SalaDto objs : SalaControladorListar.arregloSalas()) {
                    if (objs.getIdSala() == Integer.parseInt(columnas[3].trim())) {
                        salafuncion = objs;
                    }
                }
                LocalDate fechafuncion = LocalDate.parse(columnas[4].trim());
                LocalTime horafuncion = LocalTime.parse(columnas[5].trim());
                Boolean edadfuncion = Boolean.valueOf(columnas[6].trim());
                String imagPublicas = columnas[7].trim();
                String imagPrivadas = columnas[8].trim();

                arregloFunciones.add(new FuncionDto(codfuncion, nombrefuncion, salafuncion, pelifuncion, fechafuncion, horafuncion, edadfuncion, imagPublicas, imagPrivadas));

            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }
        return arregloFunciones;
    }

    @Override
    public FuncionDto insertInto(FuncionDto dto, String ruta) {
        Funcion objFuncion = new Funcion();
        Sala objSala = new Sala(
                dto.getIdSalaFuncion().getIdSala(),
                dto.getIdSalaFuncion().getNombreSala(),
                dto.getIdSalaFuncion().getIdPeliculaSala(),
                dto.getIdSalaFuncion().getEstaDisponibleSala(),
                dto.getIdSalaFuncion().getNumeroAsientosSala(),
                dto.getIdSalaFuncion().getServicioVipSala(),
                dto.getIdSalaFuncion().getNombreImagenPublicoSala(),
                dto.getIdSalaFuncion().getNombreImagenPrivadoSala()
        );

        GeneroDto generoDto = dto.getIdPeliculaFuncion().getIdGeneroPelicula();
        Genero objGenero = new Genero(
                generoDto.getIdGenero(),
                generoDto.getNombreGenero(),
                generoDto.getEstadoGenero(),
                generoDto.getCantPeliculasGenero(),
                generoDto.getCalificacionGenero(),
                generoDto.getFechaCreacionGenero(),
                generoDto.getPublicoObjetivoGenero(),
                generoDto.getNombreImagenPublicoGenero(),
                generoDto.getNombreImagenPrivadoGenero()
        );

        Pelicula objPelicula = new Pelicula(
                dto.getIdPeliculaFuncion().getIdPelicula(),
                dto.getIdPeliculaFuncion().getNombrePelicula(),
                dto.getIdPeliculaFuncion().getFechaEstrenoPelicula(),
                objGenero,
                dto.getIdPeliculaFuncion().getEs3dPelicula(),
                dto.getIdPeliculaFuncion().getClasificacionEdadPelicula(),
                dto.getIdPeliculaFuncion().getNombreImagenPublicoPelicula(),
                dto.getIdPeliculaFuncion().getNombreImagenPrivadoPelicula()
        );

        objFuncion.setIdFuncion(getSerial());
        objFuncion.setNombreFuncion(dto.getNombreFuncion());
        objFuncion.setIdSalaFuncion(objSala);
        objFuncion.setIdPeliculaFuncion(objPelicula);
        objFuncion.setFechaFuncion(dto.getFechaFuncion());
        objFuncion.setHoraFuncion(dto.getHoraFuncion());
        objFuncion.setParaMayoresFuncion(dto.getParaMayoresFuncion());
        objFuncion.setNombreImagenPublicoFuncion(dto.getNombreImagenPublicoFuncion());
        objFuncion.setNombreImagenPrivadoFuncion(GestorImagen.grabarLaImagen(ruta));

        String cadena
                = objFuncion.getIdFuncion() + Persistencia.SEPARADOR_COLUMNAS
                + objFuncion.getNombreFuncion() + Persistencia.SEPARADOR_COLUMNAS
                + objFuncion.getIdSalaFuncion().getIdSala() + Persistencia.SEPARADOR_COLUMNAS
                + objFuncion.getIdPeliculaFuncion().getIdPelicula() + Persistencia.SEPARADOR_COLUMNAS
                + objFuncion.getFechaFuncion() + Persistencia.SEPARADOR_COLUMNAS
                + objFuncion.getHoraFuncion() + Persistencia.SEPARADOR_COLUMNAS
                + objFuncion.getParaMayoresFuncion() + Persistencia.SEPARADOR_COLUMNAS
                + objFuncion.getNombreImagenPublicoFuncion() + Persistencia.SEPARADOR_COLUMNAS
                + objFuncion.getNombreImagenPrivadoFuncion();

        if (miArchivo.agregarRegistro(cadena)) {
            dto.setIdFuncion(objFuncion.getIdFuncion());
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
            Logger.getLogger(FuncionServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return codigo;
    }

    @Override
    public int numRows() {
        int cantidad = 0;
        try {
            cantidad = miArchivo.cantidadFilas();
        } catch (IOException ex) {
            Logger.getLogger(FuncionServicio.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(FuncionServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return correcto;
    }

    @Override
    public Boolean updateSet(int indice, FuncionDto obj, String ruta) {
        boolean correcto = false;

        try {
            String cadena, nocu;

            cadena
                    = obj.getIdFuncion() + Persistencia.SEPARADOR_COLUMNAS
                    + obj.getNombreFuncion() + Persistencia.SEPARADOR_COLUMNAS
                    + obj.getIdSalaFuncion().getIdSala() + Persistencia.SEPARADOR_COLUMNAS
                    + obj.getIdPeliculaFuncion().getIdPelicula() + Persistencia.SEPARADOR_COLUMNAS
                    + obj.getFechaFuncion() + Persistencia.SEPARADOR_COLUMNAS
                    + obj.getHoraFuncion() + Persistencia.SEPARADOR_COLUMNAS
                    + obj.getParaMayoresFuncion() + Persistencia.SEPARADOR_COLUMNAS
                    + obj.getNombreImagenPublicoFuncion() + Persistencia.SEPARADOR_COLUMNAS;

            if (ruta.isBlank()) {
                cadena = cadena + obj.getNombreImagenPrivadoFuncion();
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
            
        } catch (IOException e) {
            Logger.getLogger(FuncionServicio.class.getName()).log(Level.SEVERE, null, e);
        }
        return correcto;
    }

    @Override
    public FuncionDto getOne(int indice) {
        FuncionDto objListo = null;
        try {
            List<String> arrDatos = miArchivo.obtenerFila(indice);
            if (arrDatos != null && arrDatos.size() >= 9) {
                
                int codFuncion = Integer.parseInt(arrDatos.get(0).trim());
                String nombreFuncion = arrDatos.get(1).trim();
                int idPelicula = Integer.parseInt(arrDatos.get(2).trim());
                int idSala = Integer.parseInt(arrDatos.get(3).trim());
                LocalDate fechaFuncion = LocalDate.parse(arrDatos.get(4).trim());
                LocalTime horaFuncion = LocalTime.parse(arrDatos.get(5).trim());
                Boolean edadFuncion = Boolean.valueOf(arrDatos.get(6).trim());
                String imagPublicas = arrDatos.get(7).trim();
                String imagPrivadas = arrDatos.get(8).trim();
                    
                PeliculaDto Peliculafunc = null;
                for (PeliculaDto objp : PeliculaControladorListar.arregloPeliculas()) {
                    if (objp.getIdPelicula() == idPelicula) {
                        Peliculafunc = objp;
                        break;
                    }
                }
                SalaDto Salafunc = null;
                for (SalaDto objs : SalaControladorListar.arregloSalas()) {
                    if (objs.getIdSala() == idSala) {
                        Salafunc = objs;
                        break;
                    }
                }
                
                if (Peliculafunc != null && Salafunc != null) {
                    objListo = new FuncionDto(codFuncion, nombreFuncion, Salafunc, Peliculafunc, fechaFuncion, horaFuncion, edadFuncion, imagPublicas, imagPrivadas);
                }
                
            }
        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(FuncionServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return objListo;
    }

}
