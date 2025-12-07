import json
import requests
import sys
from collections import defaultdict

def load_spec(file_path):
    """Load OpenAPI spec"""
    with open(file_path, 'r', encoding='utf-8') as f:
        return json.load(f)

def extract_spec_endpoints(spec_data):
    """Extract all endpoints from spec"""
    endpoints = []
    if 'paths' in spec_data:
        for path, methods in spec_data['paths'].items():
            for method, details in methods.items():
                if method.lower() in ['get', 'post', 'put', 'delete', 'patch']:
                    tags = details.get('tags', ['untagged'])
                    endpoints.append({
                        'method': method.upper(),
                        'path': path,
                        'tag': tags[0] if tags else 'untagged',
                        'summary': details.get('summary', ''),
                        'operationId': details.get('operationId', '')
                    })
    return endpoints

def test_endpoint(base_url, method, path, token=None):
    """Test if endpoint exists by making HTTP request"""
    # Replace path parameters with dummy values
    test_path = path
    test_path = test_path.replace('{id}', '1')
    test_path = test_path.replace('{code}', 'test')
    test_path = test_path.replace('{sku}', 'TEST-SKU')
    test_path = test_path.replace('{email}', 'test@test.com')
    test_path = test_path.replace('{productId}', '1')
    test_path = test_path.replace('{categoryId}', '1')
    test_path = test_path.replace('{merchantId}', '1')
    test_path = test_path.replace('{store}', 'DEFAULT')
    test_path = test_path.replace('{token}', 'test-token')
    test_path = test_path.replace('{storeId}', '1')
    test_path = test_path.replace('{reviewId}', '1')
    test_path = test_path.replace('{reviewid}', '1')
    test_path = test_path.replace('{attributeId}', '1')
    test_path = test_path.replace('{optionId}', '1')
    test_path = test_path.replace('{imageId}', '1')
    test_path = test_path.replace('{variantId}', '1')
    test_path = test_path.replace('{variationId}', '1')
    test_path = test_path.replace('{entryId}', '1')
    test_path = test_path.replace('{parent}', '1')
    test_path = test_path.replace('{friendlyUrl}', 'test-product')
    test_path = test_path.replace('{name}', 'test')
    test_path = test_path.replace('{promo}', 'PROMO10')
    test_path = test_path.replace('{group}', 'ADMIN')
    test_path = test_path.replace('{ProductId}', '1')
    
    url = base_url + test_path
    headers = {}
    if token:
        headers['Authorization'] = f'Bearer {token}'
    
    try:
        if method == 'GET':
            response = requests.get(url, headers=headers, timeout=10)
        elif method == 'POST':
            response = requests.post(url, headers=headers, json={}, timeout=10)
        elif method == 'PUT':
            response = requests.put(url, headers=headers, json={}, timeout=10)
        elif method == 'DELETE':
            response = requests.delete(url, headers=headers, timeout=10)
        elif method == 'PATCH':
            response = requests.patch(url, headers=headers, json={}, timeout=10)
        else:
            return 'UNKNOWN', 0
        
        # 404 = not found, anything else means endpoint exists
        if response.status_code == 404:
            return 'NOT_FOUND', response.status_code
        else:
            return 'EXISTS', response.status_code
    except requests.exceptions.Timeout:
        return 'TIMEOUT', 0
    except requests.exceptions.ConnectionError:
        return 'CONNECTION_ERROR', 0
    except Exception as e:
        return 'ERROR', 0

def main():
    spec_file = 'api-gateway/online-api-docs.json'
    base_url = 'http://localhost:8080'  # API Gateway
    
    print(f"Loading spec from {spec_file}...")
    spec_data = load_spec(spec_file)
    endpoints = extract_spec_endpoints(spec_data)
    
    print(f"Found {len(endpoints)} endpoints in spec")
    print(f"Testing against {base_url}...")
    print("=" * 80)
    
    results = {
        'EXISTS': [],
        'NOT_FOUND': [],
        'CONNECTION_ERROR': [],
        'TIMEOUT': [],
        'ERROR': []
    }
    
    for i, ep in enumerate(endpoints, 1):
        status, code = test_endpoint(base_url, ep['method'], ep['path'])
        results[status].append({**ep, 'status_code': code})
        
        if i % 10 == 0:
            print(f"Tested {i}/{len(endpoints)} endpoints...", end='\r')
    
    print(f"\nTested {len(endpoints)} endpoints")
    print("=" * 80)
    print("\nRESULTS:")
    print(f"  EXISTS (implemented): {len(results['EXISTS'])}")
    print(f"  NOT_FOUND (missing): {len(results['NOT_FOUND'])}")
    print(f"  CONNECTION_ERROR: {len(results['CONNECTION_ERROR'])}")
    print(f"  TIMEOUT: {len(results['TIMEOUT'])}")
    print(f"  ERROR: {len(results['ERROR'])}")
    
    if results['CONNECTION_ERROR']:
        print("\n⚠️  CONNECTION ERROR - Is the API Gateway running on http://localhost:8080?")
        print("   Start services first: kubectl port-forward -n online-spaza service/api-gateway 8080:8080")
        return
    
    coverage = (len(results['EXISTS']) / len(endpoints) * 100) if endpoints else 0
    print(f"\nCOVERAGE: {coverage:.1f}%")
    
    # Group missing by tag
    if results['NOT_FOUND']:
        print("\n" + "=" * 80)
        print("MISSING ENDPOINTS BY SERVICE:")
        print("=" * 80)
        by_tag = defaultdict(list)
        for ep in results['NOT_FOUND']:
            by_tag[ep['tag']].append(ep)
        
        for tag in sorted(by_tag.keys()):
            eps = by_tag[tag]
            print(f"\n{tag} ({len(eps)} missing):")
            for ep in eps[:5]:
                print(f"  {ep['method']:6} {ep['path']}")
                print(f"         {ep['summary']}")
            if len(eps) > 5:
                print(f"  ... and {len(eps) - 5} more")
    
    # Save detailed results
    with open('endpoint_test_results.json', 'w') as f:
        json.dump(results, f, indent=2)
    print(f"\nDetailed results saved to: endpoint_test_results.json")

if __name__ == "__main__":
    main()
