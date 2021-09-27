import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'clinicas',
        data: { pageTitle: 'safhApp.clinicas.home.title' },
        loadChildren: () => import('./clinicas/clinicas.module').then(m => m.ClinicasModule),
      },
      {
        path: 'dietas',
        data: { pageTitle: 'safhApp.dietas.home.title' },
        loadChildren: () => import('./dietas/dietas.module').then(m => m.DietasModule),
      },
      {
        path: 'enfermarias',
        data: { pageTitle: 'safhApp.enfermarias.home.title' },
        loadChildren: () => import('./enfermarias/enfermarias.module').then(m => m.EnfermariasModule),
      },
      {
        path: 'especialidades',
        data: { pageTitle: 'safhApp.especialidades.home.title' },
        loadChildren: () => import('./especialidades/especialidades.module').then(m => m.EspecialidadesModule),
      },
      {
        path: 'fabricantes',
        data: { pageTitle: 'safhApp.fabricantes.home.title' },
        loadChildren: () => import('./fabricantes/fabricantes.module').then(m => m.FabricantesModule),
      },
      {
        path: 'internacoes',
        data: { pageTitle: 'safhApp.internacoes.home.title' },
        loadChildren: () => import('./internacoes/internacoes.module').then(m => m.InternacoesModule),
      },
      {
        path: 'internacoes-detalhes',
        data: { pageTitle: 'safhApp.internacoesDetalhes.home.title' },
        loadChildren: () => import('./internacoes-detalhes/internacoes-detalhes.module').then(m => m.InternacoesDetalhesModule),
      },
      {
        path: 'leitos',
        data: { pageTitle: 'safhApp.leitos.home.title' },
        loadChildren: () => import('./leitos/leitos.module').then(m => m.LeitosModule),
      },
      {
        path: 'medicamentos',
        data: { pageTitle: 'safhApp.medicamentos.home.title' },
        loadChildren: () => import('./medicamentos/medicamentos.module').then(m => m.MedicamentosModule),
      },
      {
        path: 'medicos',
        data: { pageTitle: 'safhApp.medicos.home.title' },
        loadChildren: () => import('./medicos/medicos.module').then(m => m.MedicosModule),
      },
      {
        path: 'pacientes',
        data: { pageTitle: 'safhApp.pacientes.home.title' },
        loadChildren: () => import('./pacientes/pacientes.module').then(m => m.PacientesModule),
      },
      {
        path: 'posologias',
        data: { pageTitle: 'safhApp.posologias.home.title' },
        loadChildren: () => import('./posologias/posologias.module').then(m => m.PosologiasModule),
      },
      {
        path: 'prescricoes',
        data: { pageTitle: 'safhApp.prescricoes.home.title' },
        loadChildren: () => import('./prescricoes/prescricoes.module').then(m => m.PrescricoesModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
