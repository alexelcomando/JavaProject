package com.unimagdalena.servicio;

import com.poo.persistence.NioFile;
import com.unimagdalena.api.ApiOperacionBD;
import com.unimagdalena.controlador.pelicula.PeliculaControladorListar;
import com.unimagdalena.dto.ClienteDto;
import com.unimagdalena.dto.PeliculaDto;
import com.unimagdalena.model.Cliente;
import com.unimagdalena.model.Pelicula;
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

public class ClienteServicio implements ApiOperacionBD<ClienteDto> {

    private NioFile miArchivo;

    public ClienteServicio() {
        try {
            miArchivo = new NioFile(Persistencia.NOMBRE_CLIENTE);
        } catch (IOException ex) {
            Logger.getLogger(ClienteServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<ClienteDto> SelectFrom() {
        List<ClienteDto> lista = new ArrayList<>();
        List<String> datos = miArchivo.obtenerDatos();
        List<PeliculaDto> peliculas = PeliculaControladorListar.arregloPeliculas();

        for (String fila : datos) {
            try {
                String limpiarFila = fila.replace("@", ""); 
                String[] c = limpiarFila.split(Persistencia.SEPARADOR_COLUMNAS);
                
                if (c.length < 9) continue;

                int idPelicula = Integer.parseInt(c[6].trim());
                PeliculaDto peliculaCliente = peliculas.stream()
                    .filter(p -> p.getIdPelicula() == idPelicula)
                    .findFirst().orElse(null);

                ClienteDto dto = new ClienteDto();
                dto.setIdCliente(Integer.parseInt(c[0].trim()));
                dto.setNombreCliente(c[1].trim());
                dto.setTipoDocumentoCliente(c[2].trim());
                dto.setFechaDeNacimientoCliente(LocalDate.parse(c[3].trim()));
                dto.setSexoCliente(Boolean.parseBoolean(c[4].trim()));
                dto.setEsAccesoVipCliente(c[5].trim());
                dto.setIdPeliculaCliente(peliculaCliente);
                dto.setNombreImagenPublicoCliente(c[7].trim());
                dto.setNombreImagenPrivadoCliente(c[8].trim());

                lista.add(dto);
            } catch (Exception e) {
                System.out.println("Error al leer cliente: " + e.getMessage());
            }
        }
        return lista;
    }

    @Override
    public ClienteDto insertInto(ClienteDto dto, String ruta) {
        Cliente obj = new Cliente();
        Pelicula objPelicula = null;
        if (dto.getIdPeliculaCliente() != null) {
            objPelicula = new Pelicula();
            objPelicula.setIdPelicula(dto.getIdPeliculaCliente().getIdPelicula());
        }

        obj.setIdCliente(getSerial());
        obj.setNombreCliente(dto.getNombreCliente());
        obj.setTipoDocumentoCliente(dto.getTipoDocumentoCliente());
        obj.setFechaDeNacimientoCliente(dto.getFechaDeNacimientoCliente());
        obj.setSexoCliente(dto.getSexoCliente());
        obj.setEsAccesoVipCliente(dto.getEsAccesoVipCliente());
        obj.setIdPeliculaCliente(objPelicula);
        obj.setNombreImagenPublicoCliente(dto.getNombreImagenPublicoCliente());
        obj.setNombreImagenPrivadoCliente(GestorImagen.grabarLaImagen(ruta));
        
        int idPelicula = (objPelicula != null) ? objPelicula.getIdPelicula() : 0;

        String linea = obj.getIdCliente() + Persistencia.SEPARADOR_COLUMNAS
                + obj.getNombreCliente() + Persistencia.SEPARADOR_COLUMNAS
                + obj.getTipoDocumentoCliente() + Persistencia.SEPARADOR_COLUMNAS
                + obj.getFechaDeNacimientoCliente() + Persistencia.SEPARADOR_COLUMNAS
                + obj.getSexoCliente() + Persistencia.SEPARADOR_COLUMNAS
                + obj.getEsAccesoVipCliente() + Persistencia.SEPARADOR_COLUMNAS
                + idPelicula + Persistencia.SEPARADOR_COLUMNAS
                + obj.getNombreImagenPublicoCliente() + Persistencia.SEPARADOR_COLUMNAS
                + obj.getNombreImagenPrivadoCliente();

        if (miArchivo.agregarRegistro(linea)) {
            dto.setIdCliente(obj.getIdCliente());
            return dto;
        }
        return null;
    }

    @Override
    public Boolean updateSet(int indice, ClienteDto dto, String ruta) {
        try {
            String imgPriv = dto.getNombreImagenPrivadoCliente();
            
            if (ruta != null && !ruta.isBlank()) {
                imgPriv = GestorImagen.grabarLaImagen(ruta);
                List<String> filaAnt = miArchivo.obtenerFila(indice);
                if (filaAnt != null && filaAnt.size() > 8) {
                    Files.deleteIfExists(Paths.get(Persistencia.RUTA_IMAGENES_EXTERNAS, filaAnt.get(8).trim().replace("@", "")));
                }
            }
            
            int idPelicula = (dto.getIdPeliculaCliente() != null) ? dto.getIdPeliculaCliente().getIdPelicula() : 0;

            String linea = dto.getIdCliente() + Persistencia.SEPARADOR_COLUMNAS
                    + dto.getNombreCliente() + Persistencia.SEPARADOR_COLUMNAS
                    + dto.getTipoDocumentoCliente() + Persistencia.SEPARADOR_COLUMNAS
                    + dto.getFechaDeNacimientoCliente() + Persistencia.SEPARADOR_COLUMNAS
                    + dto.getSexoCliente() + Persistencia.SEPARADOR_COLUMNAS
                    + dto.getEsAccesoVipCliente() + Persistencia.SEPARADOR_COLUMNAS
                    + idPelicula + Persistencia.SEPARADOR_COLUMNAS
                    + dto.getNombreImagenPublicoCliente() + Persistencia.SEPARADOR_COLUMNAS
                    + imgPriv;

            return miArchivo.actualizaFilaPosicion(indice, linea);
        } catch (IOException e) {
            Logger.getLogger(ClienteServicio.class.getName()).log(Level.SEVERE, "Error en updateSet", e);
            return false;
        }
    }

    @Override
    public Boolean deleteFrom(int indice) {
        try {
            List<String> fila = miArchivo.obtenerFila(indice);
            if (fila != null && !fila.isEmpty()) {
               
            }
            return !miArchivo.borrarFilaPosicion(indice).isEmpty();
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public int getSerial() {
        try {
            return miArchivo.ultimoCodigo() + 1;
        } catch (IOException e) {
            return 1;
        }
    }

    @Override
    public int numRows() {
        try {
            return miArchivo.cantidadFilas();
        } catch (IOException e) {
            return 0;
        }
    }

    @Override
    public ClienteDto getOne(int indice) {
        ClienteDto objListo = null;
        List<PeliculaDto> peliculas = PeliculaControladorListar.arregloPeliculas();

        try {
            List<String> columnas = miArchivo.obtenerFila(indice);
            
            if (columnas != null && columnas.size() >= 9) {
                
                String imgPrivClean = columnas.get(8).trim().replace("@", "");
                int idPelicula = Integer.parseInt(columnas.get(6).trim());
                
                PeliculaDto peliculaCliente = peliculas.stream()
                    .filter(p -> p.getIdPelicula() == idPelicula)
                    .findFirst().orElse(null);

                objListo = new ClienteDto();
                objListo.setIdCliente(Integer.parseInt(columnas.get(0).trim()));
                objListo.setNombreCliente(columnas.get(1).trim());
                objListo.setTipoDocumentoCliente(columnas.get(2).trim());
                objListo.setFechaDeNacimientoCliente(LocalDate.parse(columnas.get(3).trim()));
                objListo.setSexoCliente(Boolean.parseBoolean(columnas.get(4).trim()));
                objListo.setEsAccesoVipCliente(columnas.get(5).trim());
                objListo.setIdPeliculaCliente(peliculaCliente);
                objListo.setNombreImagenPublicoCliente(columnas.get(7).trim());
                objListo.setNombreImagenPrivadoCliente(imgPrivClean);
            }
        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(ClienteServicio.class.getName()).log(Level.SEVERE, "Error al obtener cliente por índice", ex);
        }
        return objListo;
    }
}