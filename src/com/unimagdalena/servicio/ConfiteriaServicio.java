package com.unimagdalena.servicio;

import com.unimagdalena.api.ApiOperacionBD;
import com.unimagdalena.dto.ConfiteriaDto;
import com.unimagdalena.helpers.constante.Persistencia;
import com.unimagdalena.helpers.utilidad.GestorImagen;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfiteriaServicio implements ApiOperacionBD<ConfiteriaDto> {

    private final Path archivo;

    public ConfiteriaServicio() {
        archivo = Paths.get(Persistencia.NOMBRE_CONFITERIA);
        try {
            if (!Files.exists(archivo)) {
                Files.createDirectories(archivo.getParent());
                Files.createFile(archivo);
            }
        } catch (IOException e) {
            Logger.getLogger(ConfiteriaServicio.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    // ==========================================================
    // SERIAL
    // ==========================================================
    @Override
    public int getSerial() {
        try {
            List<String> lineas = Files.readAllLines(archivo);
            if (lineas.isEmpty()) return 1;

            String ultima = lineas.get(lineas.size() - 1)
                    .replace("@", "")
                    .trim();

            String[] columnas = ultima.split(Persistencia.SEPARADOR_COLUMNAS);
            return Integer.parseInt(columnas[0]) + 1;

        } catch (Exception e) {
            return 1;
        }
    }

    // ==========================================================
    // INSERT
    // ==========================================================
    @Override
    public ConfiteriaDto insertInto(ConfiteriaDto dto, String ruta) {

        int codigo = getSerial();

        // Imagen privada (solo ruta interna)
        String nombreImagenPrivada = dto.getNombreImagenPrivadoConfiteria();
        if (ruta != null && !ruta.isBlank()) {
            nombreImagenPrivada = GestorImagen.grabarLaImagen(ruta);
        }

        // Cadena para guardar
        String cadena = codigo + Persistencia.SEPARADOR_COLUMNAS +
                dto.getNombreProductoConfiteria() + Persistencia.SEPARADOR_COLUMNAS +
                String.format(java.util.Locale.US, "%.2f", dto.getPrecioProductoConfiteria())
        + Persistencia.SEPARADOR_COLUMNAS

                
                +dto.getTipoProductoConfiteria() + Persistencia.SEPARADOR_COLUMNAS +
                dto.getProductoDisponibleConfiteria() + Persistencia.SEPARADOR_COLUMNAS +
                dto.getFechaCompraProducto() + Persistencia.SEPARADOR_COLUMNAS +
                dto.getMetodoPagoProducto() + Persistencia.SEPARADOR_COLUMNAS +
                dto.getNombreImagenPublicoConfiteria() + Persistencia.SEPARADOR_COLUMNAS +
                nombreImagenPrivada;

        try {
            Files.writeString(archivo, cadena + "\n", StandardOpenOption.APPEND);
        } catch (IOException e) {
            Logger.getLogger(ConfiteriaServicio.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }

        dto.setIdConfiteria(codigo);
        dto.setNombreImagenPrivadoConfiteria(nombreImagenPrivada);
        return dto;
    }

    // ==========================================================
    // SELECT ALL
    // ==========================================================
    @Override
    public List<ConfiteriaDto> SelectFrom() {

        List<ConfiteriaDto> lista = new ArrayList<>();

        try {
            List<String> filas = Files.readAllLines(archivo);

            for (String reg : filas) {
                if (reg.isBlank()) continue;

                reg = reg.replace("@", "");
                String[] c = reg.split(Persistencia.SEPARADOR_COLUMNAS);

                ConfiteriaDto dto = new ConfiteriaDto(
                        Integer.parseInt(c[0].trim()),
                        c[1].trim(),
                        Double.parseDouble(c[2].trim().replace(",", ".")),

                        c[3].trim(),
                        Boolean.parseBoolean(c[4].trim()),
                        java.time.LocalDate.parse(c[5].trim()),
                        c[6].trim(),
                        c[7].trim(),
                        c[8].trim()
                );

                lista.add(dto);
            }

        } catch (IOException ex) {
            Logger.getLogger(ConfiteriaServicio.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lista;
    }

    // ==========================================================
    // NUM ROWS
    // ==========================================================
    @Override
    public int numRows() {
        try {
            return (int) Files.lines(archivo).count();
        } catch (IOException ex) {
            return 0;
        }
    }

    // ==========================================================
    // DELETE
    // ==========================================================
    @Override
    public Boolean deleteFrom(int codigo) {
        try {
            List<String> filas = Files.readAllLines(archivo);
            List<String> nuevas = new ArrayList<>();

            for (String f : filas) {
                if (!f.startsWith(codigo + Persistencia.SEPARADOR_COLUMNAS)) {
                    nuevas.add(f);
                }
            }

            Files.write(archivo, nuevas);
            return true;

        } catch (IOException ex) {
            return false;
        }
    }

    // ==========================================================
    // UPDATE
    // ==========================================================
    @Override
    public Boolean updateSet(int codigo, ConfiteriaDto dto, String ruta) {

        ConfiteriaDto original = getOne(codigo);
        if (original == null) return false;

        String nombreImagenPrivada = original.getNombreImagenPrivadoConfiteria();

        if (ruta != null && !ruta.isBlank()) {
            nombreImagenPrivada = GestorImagen.grabarLaImagen(ruta);
        }

        String nuevo = codigo + Persistencia.SEPARADOR_COLUMNAS +
                dto.getNombreProductoConfiteria() + Persistencia.SEPARADOR_COLUMNAS +
                String.format(java.util.Locale.US, "%.2f", dto.getPrecioProductoConfiteria())
 + Persistencia.SEPARADOR_COLUMNAS +
                dto.getTipoProductoConfiteria() + Persistencia.SEPARADOR_COLUMNAS +
                dto.getProductoDisponibleConfiteria() + Persistencia.SEPARADOR_COLUMNAS +
                dto.getFechaCompraProducto() + Persistencia.SEPARADOR_COLUMNAS +
                dto.getMetodoPagoProducto() + Persistencia.SEPARADOR_COLUMNAS +
                dto.getNombreImagenPublicoConfiteria() + Persistencia.SEPARADOR_COLUMNAS +
                nombreImagenPrivada;

        try {
            List<String> filas = Files.readAllLines(archivo);
            List<String> nuevas = new ArrayList<>();

            for (String f : filas) {
                if (f.startsWith(codigo + Persistencia.SEPARADOR_COLUMNAS))
                    nuevas.add(nuevo);
                else
                    nuevas.add(f);
            }

            Files.write(archivo, nuevas, StandardOpenOption.TRUNCATE_EXISTING);
            return true;

        } catch (IOException ex) {
            return false;
        }
    }

    // ==========================================================
    // GET ONE
    // ==========================================================
    @Override
    public ConfiteriaDto getOne(int codigo) {
        return SelectFrom()
                .stream()
                .filter(x -> x.getIdConfiteria() == codigo)
                .findFirst()
                .orElse(null);
    }
}
