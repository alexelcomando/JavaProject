package com.unimagdalena.servicio;

import com.unimagdalena.api.ApiOperacionBD;
import com.unimagdalena.dto.SalaDto;
import com.unimagdalena.dto.PeliculaDto;
import com.unimagdalena.model.Sala;
import com.unimagdalena.model.Pelicula;
import com.unimagdalena.controlador.pelicula.PeliculaControladorListar;
import com.unimagdalena.helpers.constante.Persistencia;
import com.unimagdalena.helpers.utilidad.GestorImagen;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SalaServicio implements ApiOperacionBD<SalaDto> {

    private final File archivo;

    public SalaServicio() {
        this.archivo = new File(Persistencia.NOMBRE_SALA);
        verificarArchivo();
    }

    private void verificarArchivo() {
        try {
            if (!archivo.exists()) {
                File carpeta = archivo.getParentFile();
                if (carpeta != null && !carpeta.exists()) {
                    carpeta.mkdirs();
                }
                archivo.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Error al crear el archivo de salas: " + e.getMessage());
        }
    }

    @Override
    public List<SalaDto> SelectFrom() {
        List<SalaDto> salas = new ArrayList<>();

        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    SalaDto dto = parsearLinea(linea);
                    if (dto != null) {
                        salas.add(dto);
                    }
            }
        }
        
    }catch (IOException e){
            System.err.println("Error al leer salas: " + e.getMessage());
        }
return salas;}

    public List<SalaDto> selectFromWhereDisponibleTrue() {
        List<SalaDto> salasDisponibles = new ArrayList<>();

        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea =  lector.readLine();
            while (linea != null) {
                if (!linea.trim().isEmpty()) {
                    SalaDto dto = parsearLinea(linea);

                    // Solo agregar si está disponible
                    if (dto != null && dto.getEstaDisponibleSala() != null && dto.getEstaDisponibleSala()) {
                        salasDisponibles.add(dto);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer salas disponibles: " + e.getMessage());
        }

        return salasDisponibles;
    }

    @Override
    public SalaDto insertInto(SalaDto obj, String ruta) {
        try {
            // Generar ID automático
            int nuevoId = getSerial() + 1;
            obj.setIdSala(nuevoId);

            // Guardar imagen si se proporcionó
            String nombreImagenPrivada = "";
            if (ruta != null && !ruta.isEmpty()) {
                nombreImagenPrivada = GestorImagen.grabarLaImagen(ruta);
                obj.setNombreImagenPrivadoSala(nombreImagenPrivada);
            }

            // Escribir en archivo
            try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo, true))) {
                String linea = convertirALinea(obj);
                escritor.write(linea);
                escritor.newLine();
            }

            return obj;
        } catch (IOException e) {
            System.err.println("Error al insertar sala: " + e.getMessage());
            return null;
        }
    }

    @Override
    public int getSerial() {
        List<SalaDto> salas = SelectFrom();
        if (salas.isEmpty()) {
            return 0;
        }

        int maxId = 0;
        for (SalaDto sala : salas) {
            if (sala.getIdSala() > maxId) {
                maxId = sala.getIdSala();
            }
        }
        return maxId;
    }

    @Override
    public int numRows() {
        return SelectFrom().size();
    }

    @Override
    public Boolean deleteFrom(int indice) {
        List<SalaDto> salas = SelectFrom();

        if (indice < 0 || indice >= salas.size()) {
            return false;
        }

        salas.remove(indice);
        return reescribirArchivo(salas);
    }

    public Boolean delete(int idSala) {
        List<SalaDto> salas = SelectFrom();

        boolean encontrado = salas.removeIf(sala -> sala.getIdSala() == idSala);

        if (encontrado) {
            return reescribirArchivo(salas);
        }

        return false;
    }

    @Override
    public Boolean updateSet(int indice, SalaDto obj, String ruta) {
        List<SalaDto> salas = SelectFrom();

        if (indice < 0 || indice >= salas.size()) {
            return false;
        }

        // Mantener ID original
        SalaDto salaOriginal = salas.get(indice);
        obj.setIdSala(salaOriginal.getIdSala());

        // Actualizar imagen solo si se proporcionó una nueva
        if (ruta != null && !ruta.isEmpty()) {
            String nombreImagenPrivada = GestorImagen.grabarLaImagen(ruta);
            obj.setNombreImagenPrivadoSala(nombreImagenPrivada);
        } else {
            // Mantener imagen original
            obj.setNombreImagenPrivadoSala(salaOriginal.getNombreImagenPrivadoSala());
        }

        salas.set(indice, obj);
        return reescribirArchivo(salas);
    }

    @Override
    public SalaDto getOne(int indice) {
        List<SalaDto> salas = SelectFrom();

        if (indice >= 0 && indice < salas.size()) {
            return salas.get(indice);
        }

        return null;
    }

    public SalaDto getOne(int idSala, boolean porId) {
        List<SalaDto> salas = SelectFrom();

        for (SalaDto sala : salas) {
            if (sala.getIdSala() == idSala) {
                return sala;
            }
        }

        return null;
    }

    // Métodos auxiliares
    private String convertirALinea(SalaDto dto) {
        StringBuilder sb = new StringBuilder();
        sb.append(dto.getIdSala()).append(Persistencia.SEPARADOR_COLUMNAS);
        sb.append(dto.getNombreSala()).append(Persistencia.SEPARADOR_COLUMNAS);

        // Película: guardar solo el ID
        if (dto.getIdPeliculaSala() != null) {
            sb.append(dto.getIdPeliculaSala().getIdPelicula());
        } else {
            sb.append("0");
        }
        sb.append(Persistencia.SEPARADOR_COLUMNAS);

        sb.append(dto.getEstaDisponibleSala()).append(Persistencia.SEPARADOR_COLUMNAS);
        sb.append(dto.getNumeroAsientosSala()).append(Persistencia.SEPARADOR_COLUMNAS);
        sb.append(dto.getServicioVipSala()).append(Persistencia.SEPARADOR_COLUMNAS);
        sb.append(dto.getNombreImagenPublicoSala() != null ? dto.getNombreImagenPublicoSala() : "").append(Persistencia.SEPARADOR_COLUMNAS);
        sb.append(dto.getNombreImagenPrivadoSala() != null ? dto.getNombreImagenPrivadoSala() : "");

        return sb.toString();
    }

    private SalaDto parsearLinea(String linea) {
        try {
            String[] partes = linea.split(Persistencia.SEPARADOR_COLUMNAS);

            if (partes.length < 8) {
                return null;
            }

            SalaDto dto = new SalaDto();
            dto.setIdSala(Integer.parseInt(partes[0]));
            dto.setNombreSala(partes[1]);

            // Cargar película: buscar la película por ID y crear el objeto Pelicula
            int idPelicula = Integer.parseInt(partes[2]);
            if (idPelicula > 0) {
                // Buscar la película en la lista de películas
                List<PeliculaDto> peliculas = PeliculaControladorListar.arregloPeliculas();
                PeliculaDto peliculaDto = null;

                for (PeliculaDto p : peliculas) {
                    if (p.getIdPelicula() == idPelicula) {
                        peliculaDto = p;
                        break;
                    }
                }

                if (peliculaDto != null) {
                    // Convertir PeliculaDto a Pelicula (modelo)
                    Pelicula pelicula = new Pelicula();
                    pelicula.setIdPelicula(peliculaDto.getIdPelicula());
                    pelicula.setNombrePelicula(peliculaDto.getNombrePelicula());
                    pelicula.setFechaEstrenoPelicula(peliculaDto.getFechaEstrenoPelicula());
                    pelicula.setEs3dPelicula(peliculaDto.getEs3dPelicula());
                    pelicula.setClasificacionEdadPelicula(peliculaDto.getClasificacionEdadPelicula());
                    pelicula.setNombreImagenPublicoPelicula(peliculaDto.getNombreImagenPublicoPelicula());
                    pelicula.setNombreImagenPrivadoPelicula(peliculaDto.getNombreImagenPrivadoPelicula());

                    dto.setIdPeliculaSala(pelicula);
                }
            }

            dto.setEstaDisponibleSala(Boolean.parseBoolean(partes[3]));
            dto.setNumeroAsientosSala(Integer.parseInt(partes[4]));
            dto.setServicioVipSala(Boolean.parseBoolean(partes[5]));
            dto.setNombreImagenPublicoSala(partes[6]);
            dto.setNombreImagenPrivadoSala(partes[7]);

            return dto;
        } catch (Exception e) {
            System.err.println("Error al parsear línea: " + e.getMessage());
            return null;
        }
    }

    private Boolean reescribirArchivo(List<SalaDto> salas) {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo))) {
            for (SalaDto sala : salas) {
                String linea = convertirALinea(sala);
                escritor.write(linea);
                escritor.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error al reescribir archivo: " + e.getMessage());
            return false;
        }
    }

    // Método auxiliar para insertar (sin ruta)
    public boolean insert(SalaDto dto, String ruta) {
        return insertInto(dto, ruta) != null;
    }
}
