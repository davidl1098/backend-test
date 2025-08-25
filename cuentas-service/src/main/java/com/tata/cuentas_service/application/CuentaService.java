package com.tata.cuentas_service.application;

import com.tata.cuentas_service.web.dto.CuentaDto;
import java.util.List;
import java.util.UUID;

public interface CuentaService {
  CuentaDto create(CuentaDto dto);

  CuentaDto get(UUID id);

  CuentaDto update(UUID id, CuentaDto dto);

  void delete(UUID id);

  List<CuentaDto> list();
}
