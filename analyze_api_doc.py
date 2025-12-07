import json
import sys
import os
import re
from pathlib import Path
from collections import defaultdict

def analyze_api_doc(file_path):
    """Analyze API documentation JSON file and extract key information"""
    
    print(f"Analyzing: {file_path}\n")
    
    with open(file_path, 'r', encoding='utf-8') as f:
        data = json.load(f)
    
    # Basic structure
    print("=== API DOCUMENTATION STRUCTURE ===\n")
    print(f"OpenAPI Version: {data.get('openapi', 'N/A')}")
    
    # Info section
    if 'info' in data:
        info = data['info']
        print(f"Title: {info.get('title', 'N/A')}")
        print(f"Version: {info.get('version', 'N/A')}")
        print(f"Description: {info.get('description', 'N/A')[:100]}...")
    
    # Servers
    if 'servers' in data:
        print(f"\n=== SERVERS ({len(data['servers'])}) ===")
        for server in data['servers']:
            print(f"  - {server.get('url', 'N/A')}")
    
    # Paths/Endpoints
    if 'paths' in data:
        paths = data['paths']
        print(f"\n=== ENDPOINTS ({len(paths)}) ===")
        
        endpoints_by_service = {}
        
        for path, methods in paths.items():
            for method, details in methods.items():
                if method in ['get', 'post', 'put', 'delete', 'patch']:
                    tags = details.get('tags', ['untagged'])
                    service = tags[0] if tags else 'untagged'
                    
                    if service not in endpoints_by_service:
                        endpoints_by_service[service] = []
                    
                    endpoints_by_service[service].append({
                        'method': method.upper(),
                        'path': path,
                        'summary': details.get('summary', 'N/A')
                    })
        
        for service, endpoints in sorted(endpoints_by_service.items()):
            print(f"\n{service.upper()} ({len(endpoints)} endpoints):")
            for ep in endpoints:
                print(f"  {ep['method']:6} {ep['path']}")
                print(f"         {ep['summary']}")
    
    # Components/Schemas
    if 'components' in data and 'schemas' in data['components']:
        schemas = data['components']['schemas']
        print(f"\n=== DATA MODELS ({len(schemas)}) ===")
        for schema_name in sorted(schemas.keys()):
            print(f"  - {schema_name}")
    
    # Security schemes
    if 'components' in data and 'securitySchemes' in data['components']:
        security = data['components']['securitySchemes']
        print(f"\n=== SECURITY SCHEMES ({len(security)}) ===")
        for scheme_name, scheme_details in security.items():
            print(f"  - {scheme_name}: {scheme_details.get('type', 'N/A')}")
    
    print("\n=== ANALYSIS COMPLETE ===")
    return data

def find_api_interfaces(root_dir):
    """Find all API interface and controller files"""
    interfaces = []
    for root, dirs, files in os.walk(root_dir):
        dirs[:] = [d for d in dirs if d not in ['target', 'build', '.git', 'node_modules']]
        for file in files:
            if (file.endswith('ApiCtrl.java') or 
                (file.endswith('Controller.java') and 'ApiCtrlController' not in file)):
                interfaces.append(os.path.join(root, file))
    return interfaces

def extract_endpoints_from_interface(file_path):
    """Extract endpoints from Java API interface"""
    endpoints = []
    try:
        with open(file_path, 'r', encoding='utf-8', errors='ignore') as f:
            content = f.read()
        
        # Extract class-level @RequestMapping
        base_path = ''
        class_mapping = re.search(r'@RequestMapping\("([^"]+)"\)', content)
        if class_mapping:
            base_path = class_mapping.group(1)
        
        patterns = [
            (r'@GetMapping\("([^"]+)"\)', 'GET'),
            (r'@PostMapping\("([^"]+)"\)', 'POST'),
            (r'@PutMapping\("([^"]+)"\)', 'PUT'),
            (r'@DeleteMapping\("([^"]+)"\)', 'DELETE'),
            (r'@PatchMapping\("([^"]+)"\)', 'PATCH'),
        ]
        
        for pattern, method in patterns:
            for match in re.finditer(pattern, content):
                method_path = match.group(1)
                full_path = base_path + method_path if base_path else method_path
                endpoints.append({'method': method, 'path': full_path, 'file': os.path.basename(file_path)})
    except:
        pass
    return endpoints

def normalize_path(path):
    """Normalize path for comparison"""
    return re.sub(r'\{[^}]+\}', '{var}', path).lower()

def compare_implementation(spec_data, root_dir):
    """Compare spec vs implementation"""
    print("\n" + "="*80)
    print("IMPLEMENTATION ANALYSIS")
    print("="*80)
    
    # Extract spec endpoints
    spec_endpoints = {}
    if 'paths' in spec_data:
        for path, methods in spec_data['paths'].items():
            for method, details in methods.items():
                if method.lower() in ['get', 'post', 'put', 'delete', 'patch']:
                    key = f"{method.upper()}:{normalize_path(path)}"
                    tags = details.get('tags', ['untagged'])
                    spec_endpoints[key] = {
                        'path': path,
                        'method': method.upper(),
                        'tag': tags[0] if tags else 'untagged',
                        'summary': details.get('summary', '')
                    }
    
    # Extract implemented endpoints
    interfaces = find_api_interfaces(root_dir)
    impl_endpoints = {}
    for interface in interfaces:
        for ep in extract_endpoints_from_interface(interface):
            key = f"{ep['method']}:{normalize_path(ep['path'])}"
            impl_endpoints[key] = ep
    
    print(f"\nSpec Endpoints: {len(spec_endpoints)}")
    print(f"Implemented Endpoints: {len(impl_endpoints)}")
    print(f"API Interface Files: {len(interfaces)}")
    
    # Find matches and mismatches
    matched = set(spec_endpoints.keys()) & set(impl_endpoints.keys())
    missing = set(spec_endpoints.keys()) - set(impl_endpoints.keys())
    extra = set(impl_endpoints.keys()) - set(spec_endpoints.keys())
    
    coverage = (len(matched) / len(spec_endpoints) * 100) if spec_endpoints else 0
    
    print(f"\nMatched: {len(matched)} ({coverage:.1f}%)")
    print(f"Missing: {len(missing)}")
    print(f"Extra (not in spec): {len(extra)}")
    
    # Show extra endpoints
    if extra:
        print("\n" + "="*80)
        print("EXTRA ENDPOINTS (NOT IN SPEC)")
        print("="*80)
        by_file_extra = defaultdict(list)
        for key in extra:
            ep = impl_endpoints[key]
            by_file_extra[ep['file']].append(ep)
        
        for file in sorted(by_file_extra.keys()):
            eps = by_file_extra[file]
            print(f"\n{file} ({len(eps)} extra):")
            for ep in sorted(eps, key=lambda x: x['path']):
                print(f"  {ep['method']:6} {ep['path']}")
    
    # Group missing by tag
    if missing:
        missing_by_tag = defaultdict(list)
        for key in missing:
            ep = spec_endpoints[key]
            missing_by_tag[ep['tag']].append(ep)
        
        print("\n" + "="*80)
        print("MISSING ENDPOINTS BY SERVICE")
        print("="*80)
        
        for tag in sorted(missing_by_tag.keys()):
            eps = missing_by_tag[tag]
            print(f"\n{tag} ({len(eps)} missing):")
            for ep in eps[:10]:  # Show first 10
                print(f"  {ep['method']:6} {ep['path']}")
                print(f"         {ep['summary']}")
            if len(eps) > 10:
                print(f"  ... and {len(eps) - 10} more")
    
    # Show all implemented endpoints by file
    print("\n" + "="*80)
    print("ALL IMPLEMENTED ENDPOINTS BY FILE")
    print("="*80)
    
    by_file = defaultdict(list)
    for ep in impl_endpoints.values():
        by_file[ep['file']].append(ep)
    
    for file in sorted(by_file.keys()):
        eps = by_file[file]
        print(f"\n{file} ({len(eps)} endpoints)")
        for ep in sorted(eps, key=lambda x: x['path']):
            print(f"  {ep['method']:6} {ep['path']}")

if __name__ == "__main__":
    root_dir = Path(__file__).parent
    spec_file = root_dir / "api-gateway" / "online-api-docs.json"
    
    if not spec_file.exists():
        print(f"Error: File not found at {spec_file}")
        sys.exit(1)
    
    spec_data = analyze_api_doc(spec_file)
    compare_implementation(spec_data, root_dir)
