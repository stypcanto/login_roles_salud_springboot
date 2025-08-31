package com.example.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.entity.Profesional;
import com.example.backend.entity.Usuario;
import com.example.backend.repository.ProfesionalRepository;
import com.example.backend.repository.UsuarioRepository;

@Service
@Transactional
public class ProfesionalServiceImpl implements ProfesionalService {

    private final ProfesionalRepository profesionalRepository;
    private final UsuarioRepository usuarioRepository;

    public ProfesionalServiceImpl(ProfesionalRepository profesionalRepository,
                                  UsuarioRepository usuarioRepository) {
        this.profesionalRepository = profesionalRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Profesional> getAllProfesionales() {
        return profesionalRepository.findAll();
    }

    @Override
    public Profesional getProfesionalById(Long id) {
        return profesionalRepository.findById(id).orElse(null);
    }

    @Override
    public Profesional getProfesionalByUsuarioId(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario == null) return null;
        return profesionalRepository.findByUsuario(usuario).orElse(null);
    }

    @Override
    public List<Profesional> getProfesionalesByEspecialidad(Long especialidadId) {
        return profesionalRepository.findByEspecialidadId(especialidadId);
    }

    @Override
    public Profesional saveProfesional(Profesional profesional) {
        return profesionalRepository.save(profesional);
    }

    @Override
    public void deleteProfesional(Long id) {
        profesionalRepository.deleteById(id);
    }
}
