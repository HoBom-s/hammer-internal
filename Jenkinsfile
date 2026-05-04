@Library('hobom-shared-lib') _
hobomPipeline(
  serviceName:    'dev-hammer-internal',
  hostPort:       '8090',
  containerPort:  '8090',
  memory:         '1536m',
  cpus:           '2',
  envPath:        '/etc/hobom-dev/dev-hammer-internal/.env',
  addHost:        true,
  smokeCheckPath: '/actuator/health'
)
