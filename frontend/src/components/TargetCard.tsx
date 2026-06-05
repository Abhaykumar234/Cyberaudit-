interface TargetCardProps {
  target: {
    id: number
    name: string
    environment: string
    targetType: string
    endpoint: string
    postureScore: number
    activeFindings: number
    remediationRate: number
    assetsInScope: number
    description: string
  }
}

export default function TargetCard({ target }: TargetCardProps) {
  const getEnvironmentColor = (env: string) => {
    switch (env) {
      case 'Production':
        return 'bg-red-600'
      case 'Staging':
        return 'bg-yellow-600'
      case 'Development':
        return 'bg-blue-600'
      default:
        return 'bg-gray-600'
    }
  }

  const getScoreColor = (score: number) => {
    if (score >= 85) return 'text-green-400'
    if (score >= 70) return 'text-yellow-400'
    if (score >= 50) return 'text-orange-400'
    return 'text-red-400'
  }

  return (
    <div className="bg-gray-800 rounded-lg overflow-hidden border border-gray-700 hover:border-cyan-500 transition">
      {/* Header */}
      <div className="p-4 border-b border-gray-700 bg-gray-750 flex justify-between items-start">
        <div className="flex-1">
          <h3 className="font-semibold text-white">{target.name}</h3>
          <p className="text-xs text-gray-400 mt-1">{target.endpoint}</p>
        </div>
        <span className={`${getEnvironmentColor(target.environment)} text-white text-xs px-2 py-1 rounded`}>
          {target.environment}
        </span>
      </div>

      {/* Body */}
      <div className="p-4 space-y-3">
        <div>
          <p className="text-xs text-gray-400 mb-1">Description</p>
          <p className="text-sm text-gray-300 line-clamp-2">{target.description}</p>
        </div>

        <div className="grid grid-cols-2 gap-3">
          <div className="bg-gray-700 rounded p-2">
            <p className="text-xs text-gray-400">Type</p>
            <p className="text-sm font-semibold text-gray-200">{target.targetType}</p>
          </div>
          <div className="bg-gray-700 rounded p-2">
            <p className="text-xs text-gray-400">Assets</p>
            <p className="text-sm font-semibold text-gray-200">{target.assetsInScope}</p>
          </div>
        </div>

        {/* Posture Score */}
        <div>
          <div className="flex justify-between items-center mb-2">
            <p className="text-xs text-gray-400">Posture Score</p>
            <span className={`text-sm font-semibold ${getScoreColor(target.postureScore)}`}>
              {target.postureScore}%
            </span>
          </div>
          <div className="h-2 bg-gray-700 rounded-full overflow-hidden">
            <div
              className={`h-full ${
                target.postureScore >= 85
                  ? 'bg-green-500'
                  : target.postureScore >= 70
                  ? 'bg-yellow-500'
                  : target.postureScore >= 50
                  ? 'bg-orange-500'
                  : 'bg-red-500'
              }`}
              style={{ width: `${target.postureScore}%` }}
            ></div>
          </div>
        </div>

        {/* Remediation Rate */}
        <div>
          <div className="flex justify-between items-center mb-2">
            <p className="text-xs text-gray-400">Remediation Rate</p>
            <span className="text-sm font-semibold text-green-400">{target.remediationRate}%</span>
          </div>
          <div className="h-2 bg-gray-700 rounded-full overflow-hidden">
            <div
              className="h-full bg-green-500"
              style={{ width: `${target.remediationRate}%` }}
            ></div>
          </div>
        </div>

        {/* Stats */}
        <div className="grid grid-cols-2 gap-3 pt-2 border-t border-gray-700">
          <div>
            <p className="text-xs text-gray-400">Active Findings</p>
            <p className="text-lg font-semibold text-red-400">{target.activeFindings}</p>
          </div>
          <div>
            <p className="text-xs text-gray-400">Status</p>
            <p className="text-lg font-semibold text-green-400">Active</p>
          </div>
        </div>
      </div>
    </div>
  )
}
